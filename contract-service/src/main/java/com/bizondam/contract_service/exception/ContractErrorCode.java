package com.bizondam.contract_service.exception;

import com.bizondam.common.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ContractErrorCode implements BaseErrorCode {
  ESTIMATE_NOT_FOUND( "ESTIMATE_001", "견적 요청을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  CONTRACT_NOT_FINALIZED("CONTRACT_002", "아직 계약이 체결되지 않았습니다.", HttpStatus.BAD_REQUEST),
  CONTRACT_ALREADY_COMPLETED("CONTRACT_003", "계약이 존재하지 않거나 이미 완료된 상태입니다.", HttpStatus.NOT_FOUND),
  CONTRACT_NOT_FOUND("CONTRACT_004", "계약 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
  UNAUTHORIZED_ACCESS("CONTRACT_005", "해당 계약에 대한 접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

  private final String code;
  private final String message;
  private final HttpStatus status;
}