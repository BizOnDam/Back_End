package com.bizondam.company_service.client;

import com.bizondam.company_service.dto.CompanyValidateResultResponse;
import com.bizondam.company_service.dto.CompanyValidationRequest;

public class NationalTaxFallback implements NationalTaxClient {
  @Override
  public CompanyValidateResultResponse validateBusiness(CompanyValidationRequest request) {
    // 장애 시 대체 응답 생성
    CompanyValidateResultResponse fallbackResponse = new CompanyValidateResultResponse();
    fallbackResponse.setValidBusinessNumber(false);
    fallbackResponse.setAlreadyRegistered(false);
    fallbackResponse.setMessage("국세청 서비스가 일시적으로 불안정합니다. 잠시 후 다시 시도해 주세요.");
    return fallbackResponse;
  }
}