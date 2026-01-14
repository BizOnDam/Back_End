package com.bizondam.matching_service.service;

import com.bizondam.matching_service.dto.*;
import com.bizondam.matching_service.mapper.SupplierRecommendationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {
    private final SupplierRecommendationMapper mapper;
    private final OpenAiService openAiService;

    // 공급기업 추천 메인 로직
    public MatchingResultDto syncRecommend(Long requestId) {
        // 1) 요청 품목 모두 가져오기
        List<EstimateItemDto> items = mapper.findEstimateItemsByRequestId(requestId);

        // 2) per-item 추천 미리 계산
        Map<Long, List<SupplierDto>> perItemMap = items.stream()
                .collect(Collectors.toMap(
                        EstimateItemDto::getProductId,
                        this::recommendSingle
                ));

        // 3) 품목 간 공급기업 교집합 시도
        Set<String> commonBiznos = tryIntersection(items);

        // 4) 공통 공급업체 및 요약 메시지 생성
        List<SupplierDto> commonSuppliers = new ArrayList<>();
        OpenAiRecommendationDto result;

        if (!commonBiznos.isEmpty()) {
            // 교집합 기업에 대한 정보 조회
            List<String> biznoList = new ArrayList<>(commonBiznos);
            List<SupplierMetricDto> metrics = mapper.findMetricsByBiznosAndRequest(biznoList, requestId);

            // SupplierDto에 맞게 매핑
            commonSuppliers = metrics.stream()
                    .map(m -> SupplierDto.builder()
                            .detailCategoryName(m.getDetailCategoryName())
                            .supplierBizno(m.getSupplierBizno())
                            .supplierName(m.getSupplierName())
                            .matchedQuantity(m.getMatchedQuantity())
                            .transactionCount(m.getTransactionCount())
                            .leadTimeDays(m.getLeadTimeDays())
                            .build())
                    .collect(Collectors.toList());
        }

        // 5) OpenAI 호출
        try {
            Map<String, Object> gptPayload = Map.of(
                    "commonSuppliers", commonSuppliers,
                    "perItemSuppliers", perItemMap
            );
            String json = new ObjectMapper().writeValueAsString(gptPayload);
            result = openAiService.getRecommendation(json); // GPT 호출 결과 받기
        } catch (Exception e) {
            log.warn("GPT 요약 실패, 기본 메시지 사용", e);
            return commonSuppliers.isEmpty()
                    ? MatchingResultDto.perItem("공통 조달 가능 업체가 없습니다.", perItemMap)
                    : MatchingResultDto.common(
                    String.format("공급기업 추천 결과 %d개 존재합니다.", commonSuppliers.size()),
                    commonSuppliers
            );
        }

        // 6. 결과 반환
        return commonSuppliers.isEmpty()
                ? MatchingResultDto.perItem(result.getSummary(), perItemMap)
                : MatchingResultDto.common(result.getSummary(), commonSuppliers);
    }

    // 교집합 로직 (fallback 3×3 루프)
    private Set<String> tryIntersection(List<EstimateItemDto> items) {
        int[] leadExtensions = {-2, 0, 3, 7};
        double[] quantityRates = {1.0, 0.8, 0.6};

        for (int ext : leadExtensions) {
            for (double rate : quantityRates) {
                Set<String> intersection = null;
                for (EstimateItemDto item : items) {
                    long maxLeadDays =
                            DAYS.between(item.getRequestDate(), item.getDueDate());
                    int qty = (int) Math.ceil(item.getQuantity() * rate);
                    int leadDays = (int) maxLeadDays + ext;

                    SupplierSearchConditionDto cond = new SupplierSearchConditionDto(
                            item.getDetailCategoryCode(), qty, null, leadDays, item.getProductId()
                    );
                    List<SupplierDto> cand = mapper.findSuppliersByConditions(cond);
                    Set<String> biznos = cand.stream()
                            .map(SupplierDto::getSupplierBizno)
                            .collect(Collectors.toSet());

                    // 비어있으면 바로 실패
                    if (biznos.isEmpty()) {
                        intersection = Collections.emptySet();
                        break;
                    }
                    // 첫 번째 아이템이면 교집합 초기화
                    if (intersection == null) {
                        intersection = new HashSet<>(biznos);
                    } else {
                        intersection.retainAll(biznos);
                    }
                    // 교집합 비면 루프 탈출
                    if (intersection.isEmpty()) break;
                }
                // 유효한 교집합 있으면 바로 반환
                if (intersection != null && !intersection.isEmpty()) {
                    return intersection;
                }
            }
        }
        return Collections.emptySet();
    }

    // 단일 품목에 대해 fallback 전략 적용
    private List<SupplierDto> recommendSingle(EstimateItemDto item) {
        int[]   leadExtensions = {0,3,7,10};
        double[] quantityRates = {1.0,0.8,0.6};

        for (int ext : leadExtensions) {
            for (double rate : quantityRates) {
                long maxLeadDays = DAYS.between(item.getRequestDate(), item.getDueDate());
                int  qty         = (int) Math.ceil(item.getQuantity() * rate);
                int  leadDays    = (int) maxLeadDays + ext;

                SupplierSearchConditionDto c = new SupplierSearchConditionDto(
                        item.getDetailCategoryCode(), qty, null, leadDays, item.getProductId()
                );
                List<SupplierDto> cand = mapper.findSuppliersByConditions(c);
                if (!cand.isEmpty()) {
                    return cand;
                }
            }
        }
        return Collections.emptyList();
    }
}
