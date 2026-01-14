//package com.bizondam.company_service.client;
//
//import com.bizondam.company_service.dto.CompanyValidationResponse;
//import org.springframework.stereotype.Component;
//
//@Component
//public class NationalTaxFallback implements NationalTaxClient {
//
//  @Override
//  public CompanyValidationResponse validateBusiness(CompanyValidationRequestWrapper request) {
//    // 장애 시 fallback 응답 처리 (예: 기본값 또는 에러 메시지)
//    CompanyValidationResponse fallbackResponse = new CompanyValidationResponse();
//    fallbackResponse.setStatus_code("ERROR");
//    fallbackResponse.setRequest_cnt(0);
//    fallbackResponse.setValid_cnt(0);
//    fallbackResponse.setData(null); // 또는 빈 리스트로
//
//    return fallbackResponse;
//  }
//}