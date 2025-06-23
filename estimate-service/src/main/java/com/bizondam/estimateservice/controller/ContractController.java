package com.bizondam.estimateservice.controller;

import com.bizondam.common.response.BaseResponse;
import com.bizondam.estimateservice.dto.ContractDto;
import com.bizondam.estimateservice.dto.ContractHistoryDto;
import com.bizondam.estimateservice.dto.ContractListResponseDto;
import com.bizondam.estimateservice.dto.TradeSummaryDto;
import com.bizondam.estimateservice.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contracts")
public class ContractController {
  private final ContractService contractService;

  // 프론트에 정보 전달
  @Operation(summary = "견적 요청서 + 응답서 조회", description = "계약서 생성을 위한 요청 및 응답서 전체 내용 반환")
  @GetMapping("/{requestId}/{responseId}")
  public ResponseEntity<BaseResponse<ContractDto>> getContractWithResponse(
          @PathVariable Long requestId, @PathVariable Long responseId
  ) {
    ContractDto contract = contractService.getContract(requestId, responseId);
    return ResponseEntity.ok(BaseResponse.success(contract));
  }

  // CEO, STAFF 필터 추가 완료
  @Operation(summary = "진행중인 계약 리스트", description = "사용자에게 할당된 모든 계약 목록 반환")
  @GetMapping("/list")
  public ResponseEntity<BaseResponse<List<ContractListResponseDto>>> getContractList(
      @RequestHeader("X-User-Id") Long userId,
      @RequestHeader("X-User-Role") String userRole,
      @RequestParam Long companyId,
      @RequestParam String role,
      @RequestParam(required = false) LocalDate date
  ) {
    List<ContractListResponseDto> list = contractService.getContractList(userId, companyId, role, userRole, date);
    if (list == null || list.isEmpty()) {
      return ResponseEntity.ok(BaseResponse.success("리스트가 없습니다.", null));
    }
    return ResponseEntity.ok(BaseResponse.success("계약 리스트 조회", list));
  }

  // 수요 기업용
  @Operation(summary = "수요 기업 견적 요청 리스트", description = "수요 기업이 생성한 모든 요청서 목록 반환")
  @GetMapping("/requestList-buyer")
  public ResponseEntity<BaseResponse<List<ContractDto>>> getBuyerList(
      @RequestParam Long companyId,
      @RequestHeader("X-User-Role") String userRole,
      @RequestHeader("X-User-Id") Long userId
  ) {
    List<ContractDto> list = contractService.getRequestsForBuyer(companyId, userRole, userId);
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
    List<ContractDto> list = contractService.getRequestsForSupplier(companyId, userRole, userId);
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
    ContractDto contract = contractService.getContractWithoutResponse(requestId);
    if (contract == null) {
      return ResponseEntity.ok(BaseResponse.success("결과가 없습니다.", null));
    }
    return ResponseEntity.ok(BaseResponse.success(contract));
  }

  // 계약 이력(미체결, 계약 중, 계약 완료)
  @Operation(summary = "거래 이력", description = "계약 번호, 내용, 일자, 금액, 상태 반환")
  @GetMapping("/contract-summary")
  public ResponseEntity<BaseResponse<List<ContractHistoryDto>>> getContractHistory(
      @RequestParam Long companyId
  ) {
    List<ContractHistoryDto> list = contractService.getContractHistoryByCompanyId(companyId);
    if (list == null || list.isEmpty()) {
      return ResponseEntity.ok(BaseResponse.success("리스트가 없습니다.", null));
    }
    return ResponseEntity.ok(BaseResponse.success("할당된 요청 리스트 조회", list));
  }

  // 대시보드
  @Operation(summary = "전체 거래 현황", description = "전체 거래 건수, 진행 중인 계약, 완료된 계약, 대기 중인 견적")
  @GetMapping("/summary")
  public ResponseEntity<BaseResponse<TradeSummaryDto>> getTradeSummary(
      @RequestParam Long companyId
  ) {
    TradeSummaryDto summary = contractService.getTradeSummary(companyId);
    return ResponseEntity.ok(BaseResponse.success("전체 거래 현황 조회", summary));
  }

  @Operation(summary = "계약 완료 처리", description = "진행중인 계약 상태를 완료 상태로 변경")
  @PatchMapping("/{contractId}/complete")
  public ResponseEntity<BaseResponse<String>> completeContract(@PathVariable Long contractId) {
    contractService.completeContract(contractId);
    return ResponseEntity.ok(BaseResponse.success("계약이 완료 상태로 변경되었습니다.", null));
  }
}