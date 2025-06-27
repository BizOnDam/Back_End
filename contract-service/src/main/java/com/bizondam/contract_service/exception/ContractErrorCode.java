package com.bizondam.contract_service.exception;

import com.bizondam.common.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ContractErrorCode implements BaseErrorCode {
  ESTIMATE_NOT_FOUND(HttpStatus.NOT_FOUND, "ESTIMATE_001", "견적 요청을 찾을 수 없습니다."),
  CONTRACT_NOT_FINALIZED(HttpStatus.BAD_REQUEST, "CONTRACT_001", "아직 계약이 체결되지 않았습니다."),
  CONTRACT_NOT_FOUND_OR_ALREADY_COMPLETED(HttpStatus.NOT_FOUND, "CONTRACT_002", "계약이 존재하지 않거나 이미 완료된 상태입니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}