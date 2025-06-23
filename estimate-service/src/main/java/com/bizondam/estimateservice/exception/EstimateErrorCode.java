package com.bizondam.estimateservice.exception;

import com.bizondam.common.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EstimateErrorCode implements BaseErrorCode {
  ESTIMATE_NOT_FOUND(HttpStatus.NOT_FOUND, "ESTIMATE_001", "견적 요청을 찾을 수 없습니다."),
  ESTIMATE_RESPONSE_NOT_FOUND(HttpStatus.NOT_FOUND, "ESTIMATE_002", "견적 응답을 찾을 수 없습니다."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "ESTIMATE_003", "로그인이 필요합니다."),
  UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "ESTIMATE_004", "접근 권한이 없습니다."),
  INVALID_ESTIMATE_STATUS(HttpStatus.BAD_REQUEST, "ESTIMATE_005", "견적 상태가 유효하지 않습니다."),
  CONTRACT_NOT_FINALIZED(HttpStatus.BAD_REQUEST, "CONTRACT_001", "아직 계약이 체결되지 않았습니다."),
  CONTRACT_NOT_FOUND_OR_ALREADY_COMPLETED(HttpStatus.NOT_FOUND, "CONTRACT_002", "계약이 존재하지 않거나 이미 완료된 상태입니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
