package com.bizondam.estimateservice.controller;

import com.bizondam.common.response.BaseResponse;
import com.bizondam.estimateservice.dto.ContractDto;
import com.bizondam.estimateservice.dto.EstimateRequestCreateDto;
import com.bizondam.estimateservice.dto.EstimateResponseCreateDto;
import com.bizondam.estimateservice.model.EstimateRequestItem;
import com.bizondam.estimateservice.service.EstimateService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estimates")
@RequiredArgsConstructor
public class EstimateController {
  private final EstimateService estimateService;

  // 프론트에 정보 전달
  @Operation(summary = "견적 요청서 + 응답서 조회", description = "계약서 생성을 위한 요청 및 응답서 전체 내용 반환")
  @GetMapping("/{requestId}/{responseId}")
  public ResponseEntity<BaseResponse<ContractDto>> getContractWithResponse(
      @PathVariable Long requestId, @PathVariable Long responseId
  ) {
    ContractDto contract = estimateService.getContract(requestId, responseId);
    return ResponseEntity.ok(BaseResponse.success(contract));
  }

  @Operation(summary = "견적 요청서 생성", description = "수요 업체에 테이블 생성")
  @PostMapping("/create_request")
  public ResponseEntity<List<EstimateRequestItem>> createEstimate(@RequestBody EstimateRequestCreateDto dto) {
    List<EstimateRequestItem> items = estimateService.createEstimateAndReturnItems(dto);
    return ResponseEntity.ok(items);
  }

  @Operation(summary = "공급 업체 지정", description = "수요 업체에서 공급 업체를 선정한 경우")
  @PatchMapping("/{requestId}/assign-supplier")
  public ResponseEntity<BaseResponse<Void>> assignSupplier(@PathVariable Long requestId,
      @RequestParam("businessNumber") String businessNumber) {
    estimateService.assignSupplierByBusinessNumber(requestId, businessNumber);
    return ResponseEntity.ok(BaseResponse.success("공급업체 지정 완료", null));
  }

  @Operation(summary = "견적 요청서 응답 생성", description = "공급 업체에 응답 테이블 생성")
  @PostMapping("/create_response")
  public ResponseEntity<BaseResponse<Long>> createResponse(@RequestBody EstimateResponseCreateDto dto) {
    Long responseId = estimateService.createResponse(dto);
    return ResponseEntity.ok(BaseResponse.success(responseId));
  }

  @Operation(summary = "계약 미체결 - 수요 업체용", description = "수요 업체에서 계약을 거절한 경우.")
  @PatchMapping("/reject-buyer/{requestId}")
  public ResponseEntity<BaseResponse<Boolean>> rejectByBuyer(@PathVariable Long requestId) {
    estimateService.rejectByBuyer(requestId);
    return ResponseEntity.ok(BaseResponse.success(requestId + " 계약 미체결", false));
  }

  @Operation(summary = "계약 미체결 - 공급 업체용", description = "공급 업체에서 계약을 거절한 경우.")
  @PutMapping("/reject-supplier/{requestId}")
  public ResponseEntity<BaseResponse<Boolean>> rejectBySupplier(@PathVariable Long requestId, @RequestParam Long supplierUserId) {
    estimateService.rejectBySupplier(requestId, supplierUserId);
    return ResponseEntity.ok().build();
  }

  // 수요 기업용
  @Operation(summary = "수요 기업 견적 요청 리스트", description = "수요 기업이 생성한 모든 요청서 목록 반환")
  @GetMapping("/requestList-buyer")
  public ResponseEntity<BaseResponse<List<ContractDto>>> getBuyerList(
      @RequestParam Long companyId,
      @RequestHeader("X-User-Role") String userRole,
      @RequestHeader("X-User-Id") Long userId
  ) {
    List<ContractDto> list = estimateService.getRequestsForBuyer(companyId, userRole, userId);
    if (list == null || list.isEmpty()) {
      return ResponseEntity.ok(BaseResponse.success("리스트가 없습니다.", null));
    }
    return ResponseEntity.ok(BaseResponse.success("요청 리스트 조회", list));
  }

  // 공급 기업용
  @Operation(summary = "공급 기업 견적 요청 리스트", description = "공급 기업에 할당된 모든 요청서 목록 반환")
  @GetMapping("/requestList-supplier")
  public ResponseEntity<BaseResponse<List<ContractDto>>> getSupplierList(
      @RequestParam Long companyId,
      @RequestHeader("X-User-Role") String userRole,
      @RequestHeader("X-User-Id") Long userId
  ) {
    List<ContractDto> list = estimateService.getRequestsForSupplier(companyId, userRole, userId);
    if (list == null || list.isEmpty()) {
      return ResponseEntity.ok(BaseResponse.success("리스트가 없습니다.", null));
    }
    return ResponseEntity.ok(BaseResponse.success("할당된 요청 리스트 조회", list));
  }

  @Operation(summary = "공급 업체용 견적 요청서 조회", description = "요청서만 반환 (응답 없음)")
  @GetMapping("/{requestId}")
  public ResponseEntity<BaseResponse<ContractDto>> getContractWithoutResponse(
      @PathVariable Long requestId
  ) {
    ContractDto contract = estimateService.getContractWithoutResponse(requestId);
    if (contract == null) {
      return ResponseEntity.ok(BaseResponse.success("결과가 없습니다.", null));
    }
    return ResponseEntity.ok(BaseResponse.success(contract));
  }
}
