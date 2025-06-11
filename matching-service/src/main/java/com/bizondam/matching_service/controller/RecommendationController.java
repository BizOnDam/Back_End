package com.bizondam.matching_service.controller;

import com.bizondam.common.response.BaseResponse;
import com.bizondam.matching_service.dto.MatchingResultDto;
import com.bizondam.matching_service.service.MatchingService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/recommend")
public class RecommendationController {

    private final MatchingService matchingService;

    @GetMapping("/{requestId}")
    public ResponseEntity<BaseResponse<MatchingResultDto>> recommend(@PathVariable Long requestId) {
        MatchingResultDto result = matchingService.syncRecommend(requestId);
        return ResponseEntity.ok(BaseResponse.success("공급기업 추천 성공", result));
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