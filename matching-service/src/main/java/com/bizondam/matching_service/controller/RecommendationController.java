package com.bizondam.matching_service.controller;

import com.bizondam.common.response.BaseResponse;
import com.bizondam.matching_service.dto.MatchingResultDto;
import com.bizondam.matching_service.service.MatchingService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendationController {

    private final MatchingService matchingService;

    @GetMapping("/{requestId}")
    public ResponseEntity<BaseResponse<MatchingResultDto>> recommend(@PathVariable Long requestId) {
        log.info("추천 시작 - requestId: {}", requestId);
        try {
            MatchingResultDto result = matchingService.syncRecommend(requestId);
            log.info("추천 성공 - requestId: {}", requestId);
            return ResponseEntity.ok(BaseResponse.success("공급기업 추천 성공", result));
        } catch (Exception e) {
            log.error("추천 로직에서 예외 발생: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(BaseResponse.error(500, "추천 실패: 서버 오류"));
        }
    }

    @Schema(description = "공급업체 정보")
    public static record SupplierInfo(
            @Schema(description = "사업자 번호", example = "1088205682")
            String supplierBizno,
            @Schema(description = "공급업체명", example = "동작구립장애인보호작업장")
            String supplierName
    ) {}

    @Schema(
            description="조달 추천 결과",
            example = """
  {
    "summary":"... 요약 문장 ...",
    "perItemRecommendations": {}
  }
  """
    )
    public static record RecommendationResponse(
            @Schema(description = "공통 추천 요약")
            String summary,
            @Schema(description = "품목별 추천 공급업체")
            Map<String, List<SupplierInfo>> perItemRecommendations
    ) {}
}