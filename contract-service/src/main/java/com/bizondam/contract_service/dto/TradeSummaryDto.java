package com.bizondam.contract_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TradeSummaryDto {
  private int totalContracts;         // 진행 + 완료 계약 수
  private int inProgressContracts;    // 계약 중
  private int completedContracts;     // 계약 완료
  private int pendingEstimates;       // 대기 중인 견적
}