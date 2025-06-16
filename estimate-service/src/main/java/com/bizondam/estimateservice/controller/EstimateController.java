package com.bizondam.estimateservice.controller;

import com.bizondam.common.response.BaseResponse;
import com.bizondam.estimateservice.dto.EstimateRequestCreateDto;
import com.bizondam.estimateservice.dto.EstimateResponseCreateDto;
import com.bizondam.estimateservice.model.EstimateRequestItem;
import com.bizondam.estimateservice.service.EstimateService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estimates")
@RequiredArgsConstructor
public class EstimateController {
  private final EstimateService estimateService;

  @Operation(summary = "견적 요청서 생성", description = "수요 업체에 테이블 생성")
  @PostMapping("/create_request")
  public ResponseEntity<List<EstimateRequestItem>> createEstimate(@RequestBody EstimateRequestCreateDto dto) {
    List<EstimateRequestItem> items = estimateService.createEstimateAndReturnItems(dto);
    return ResponseEntity.ok(items);
  }

  @Operation(summary = "공급 업체 지정", description = "수요 업체에서 공급 업체를 선정한 경우")
  @PatchMapping("/{requestId}/assign-supplier")
  public ResponseEntity<BaseResponse<Void>> assignSupplier(@PathVariable Long requestId,
      @RequestParam("supplierCompanyId") Long supplierCompanyId) {
    estimateService.assignSupplier(requestId, supplierCompanyId);
    return ResponseEntity.ok(BaseResponse.success("공급업체 지정 완료", null));
  }

  @Operation(summary = "견적 요청서 응답 생성", description = "공급 업체에 응답 테이블 생성")
  @PostMapping("/create_response")
  public ResponseEntity<BaseResponse<Long>> createResponse(@RequestBody EstimateResponseCreateDto dto) {
    Long responseId = estimateService.createResponse(dto);
    return ResponseEntity.ok(BaseResponse.success(responseId));
  }

  @Operation(summary = "계약 체결", description = "양쪽 모두 계약을 수락한 경우")
  @PatchMapping("/{requestId}/accept")
  public ResponseEntity<BaseResponse<Boolean>> acceptContract(@PathVariable Long requestId) {
    estimateService.acceptContract(requestId);
    return ResponseEntity.ok(BaseResponse.success(requestId + " 계약 체결 성공", true));
  }

  @Operation(summary = "계약 미체결", description = "어느 한 쪽에서 계약을 거절한 경우.")
  @PatchMapping("/{requestId}/reject")
  public ResponseEntity<BaseResponse<Boolean>> rejectContract(@PathVariable Long requestId) {
    estimateService.rejectContract(requestId);
    return ResponseEntity.ok(BaseResponse.success(requestId + " 계약 미체결", false));
  }
}
