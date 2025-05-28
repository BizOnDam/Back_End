package com.bizondam.company_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyValidateResultResponse {
  private boolean validBusinessNumber;   // 국세청 검증 결과
  private boolean alreadyRegistered;     // DB 존재 여부
  private String message;
}
