package com.bizondam.company_service.exception;

import com.bizondam.common.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CompanyErrorCode implements BaseErrorCode {
  // 회사 관련
  INVALID_INPUT_VALUE("COMPANY_001", "잘못된 입력입니다.", HttpStatus.BAD_REQUEST),
  ENTITY_NOT_FOUND("COMPANY_002", "회사를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  ALREADY_EXISTS("COMPANY_003", "이미 등록된 회사입니다.", HttpStatus.CONFLICT),
  INTERNAL_ERROR("COMPANY_004", "회사 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

  // 사용자 관련
  USER_NOT_FOUND("USER_001", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
  USER_NOT_CEO("USER_002", "CEO가 아닙니다.", HttpStatus.UNAUTHORIZED),
  USER_ALREADY_DELETED("USER_003", "이미 삭제된 사용자입니다.", HttpStatus.BAD_REQUEST),
  USER_CANNOT_DELETE_SELF("USER_004", "자기 자신은 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),
  USER_CANNOT_DELETE_CEO("USER_005", "CEO는 삭제할 수 없습니다.", HttpStatus.FORBIDDEN),
  USER_CANNOT_TRANSFER_TO_SELF("USER_006", "자기 자신에게는 권한을 이전할 수 없습니다.", HttpStatus.BAD_REQUEST),
  USER_TRANSFER_TARGET_MUST_BE_STAFF("USER_007", "STAFF만 CEO로 지정할 수 있습니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}