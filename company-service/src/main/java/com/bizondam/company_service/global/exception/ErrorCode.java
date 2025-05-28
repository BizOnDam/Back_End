package com.bizondam.company_service.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
  String getCode();         // “COMPANY_001” 같은 비즈니스 코드
  String getMessage();      // 사용자에게 보여줄 메시지
  HttpStatus getStatus();   // 응답에 사용할 HTTP 상태
}