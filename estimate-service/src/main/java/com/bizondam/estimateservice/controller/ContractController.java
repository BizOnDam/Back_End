package com.bizondam.estimateservice.controller;

import com.bizondam.common.response.BaseResponse;
import com.bizondam.estimateservice.dto.ContractDto;
import com.bizondam.estimateservice.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contracts")
public class ContractController {
  private final ContractService contractService;

  @GetMapping("/{requestId}/{responseId}")
  public ResponseEntity<BaseResponse<ContractDto>> getContract(
      @PathVariable Long requestId, @PathVariable Long responseId) {
    ContractDto contract = contractService.getContract(requestId, responseId);
    return ResponseEntity.ok(BaseResponse.success(contract));
  }
}