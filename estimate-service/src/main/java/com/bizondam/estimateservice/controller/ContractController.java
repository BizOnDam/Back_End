package com.bizondam.estimateservice.controller;

import com.bizondam.common.response.BaseResponse;
import com.bizondam.estimateservice.dto.ContractDto;
import com.bizondam.estimateservice.dto.ContractListResponse;
import com.bizondam.estimateservice.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contracts")
public class ContractController {
  private final ContractService contractService;

  @Operation(summary = "견적 요청서 + 응답서 조회", description = "계약서 생성을 위한 요청 및 응답서 전체 내용 반환")
  @GetMapping("/{requestId}/{responseId}")
  public ResponseEntity<BaseResponse<ContractDto>> getContractWithResponse(
          @PathVariable Long requestId, @PathVariable Long responseId
  ) {
    ContractDto contract = contractService.getContract(requestId, responseId);
    return ResponseEntity.ok(BaseResponse.success(contract));
  }

  @Operation(summary = "견적 요청서만 조회", description = "계약서 생성을 위한 요청서만 반환 (응답 없음)")
  @GetMapping("/{requestId}")
  public ResponseEntity<BaseResponse<ContractDto>> getContractWithoutResponse(
          @PathVariable Long requestId
  ) {
    ContractDto contract = contractService.getContractWithoutResponse(requestId);
    return ResponseEntity.ok(BaseResponse.success(contract));
  }

  // 수요기업용
  @Operation(summary = "수요기업 견적 요청 리스트", description = "수요기업이 생성한 모든 요청서 목록 반환")
  @GetMapping("/for-buyer")
  public ResponseEntity<BaseResponse<List<ContractDto>>> getBuyerList(
          @RequestParam Long companyId
  ) {
    List<ContractDto> list = contractService.getRequestsForBuyer(companyId);
    return ResponseEntity.ok(BaseResponse.success("요청 리스트 조회", list));
  }

  // 공급기업용
  @Operation(summary = "공급기업 견적 요청 리스트", description = "공급기업에 할당된 모든 요청서 목록 반환")
  @GetMapping("/for-supplier")
  public ResponseEntity<BaseResponse<List<ContractDto>>> getSupplierList(
          @RequestParam Long companyId
  ) {
    List<ContractDto> list = contractService.getRequestsForSupplier(companyId);
    return ResponseEntity.ok(BaseResponse.success("할당된 요청 리스트 조회", list));
  }

  @Operation(summary = "진행중인 계약 리스트", description = "사용자에게 할당된 모든 계약 목록 반환")
  @GetMapping("/list")
  public ResponseEntity<BaseResponse<List<ContractListResponse>>> getContractList(
          @RequestParam Long userId,
          @RequestParam Long companyId,
          @RequestParam String role,
          @RequestParam(required = false) String date
  ) {
    List<ContractListResponse> list = contractService.getContractList(userId, companyId, role, date);
    return ResponseEntity.ok(BaseResponse.success("계약 리스트 조회", list));
  }
}
