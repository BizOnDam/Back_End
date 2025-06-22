package com.bizondam.estimateservice.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractHistoryDto {
  private Long contractId;
  private String details;
  private LocalDate contractDate;
  private Long totalPrice;
  private String status; // 1. 미체결 2. 계약 중 3. 계약 완료
}