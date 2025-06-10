package com.bizondam.matching_service.service;

import com.bizondam.matching_service.dto.*;
import com.bizondam.matching_service.mapper.SupplierRecommendationMapper;
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

    public MatchingResultDto syncRecommend(Long requestId) {
        // 1) 요청 품목 모두 가져오기
        List<EstimateItemDto> items = mapper.findEstimateItemsByRequestId(requestId);

        // 2) per-item 추천 미리 계산
        Map<Long, List<SupplierDto>> perItemMap = items.stream()
                .collect(Collectors.toMap(
                        EstimateItemDto::getProductId,
                        this::recommendSingle
                ));

        // 3) 교집합 시도
        Set<String> commonBiznos = tryIntersection(items);

        // 4) 교집합 없으면 summary만 설정
        if (commonBiznos.isEmpty()) {
            String noCommon = "공통 조달 가능 업체가 없습니다.";
            return MatchingResultDto.perItem(noCommon,perItemMap);
        }

        // 5) 교집합이 있으면 metrics 조회 → SupplierDto 리스트로 매핑
        List<String> biznoList = new ArrayList<>(commonBiznos);
        List<SupplierMetricDto> metrics =
                mapper.findMetricsByBiznosAndRequest(biznoList, requestId);

        List<SupplierDto> commonSuppliers = metrics.stream()
                .map(m -> SupplierDto.builder()
                        .productId(m.getProductId())
                        .supplierBizno(m.getSupplierBizno())
                        .supplierName(m.getSupplierName())
                        .matchedQuantity(m.getMatchedQuantity())
                        .transactionCount(m.getTransactionCount())
                        .leadTimeDays(m.getLeadTimeDays())
                        .build())
                .collect(Collectors.toList());

        // summary: 거래 횟수·리드타임 고려한 1순위 기업
        SupplierDto top = commonSuppliers.get(0);
        String summary = String.format(
                "견적 요청하신 제품들의 거래 횟수와 리드타임을 고려하여 1순위로 추천하는 기업은 %s입니다.",
                top.getSupplierName()
        );

        return MatchingResultDto.common(summary, commonSuppliers);
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

                    if (biznos.isEmpty()) {
                        intersection = Collections.emptySet();
                        break;
                    }
                    if (intersection == null) {
                        intersection = new HashSet<>(biznos);
                    } else {
                        intersection.retainAll(biznos);
                    }
                    if (intersection.isEmpty()) break;
                }
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
