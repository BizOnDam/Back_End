package com.bizondam.company_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyValidateResultResponse {
  private boolean validBusinessNumber;   // 국세청 검증 결과
  private boolean alreadyRegistered;     // DB 존재 여부
  private Long companyId;
  private String message;
}
