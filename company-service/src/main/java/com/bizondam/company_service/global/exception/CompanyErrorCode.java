package com.bizondam.company_service.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CompanyErrorCode implements ErrorCode {
  INVALID_INPUT_VALUE("COMPANY_001", "잘못된 입력입니다.",      HttpStatus.BAD_REQUEST),
  ENTITY_NOT_FOUND    ("COMPANY_002", "회사를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  ALREADY_EXISTS      ("COMPANY_003", "이미 등록된 회사입니다.",   HttpStatus.CONFLICT),
  INTERNAL_ERROR      ("COMPANY_004", "회사 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
