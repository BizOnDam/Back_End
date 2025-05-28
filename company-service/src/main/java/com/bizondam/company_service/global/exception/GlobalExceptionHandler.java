package com.bizondam.company_service.global.exception;

import com.bizondam.company_service.global.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  // 비즈니스 예외
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<BaseResponse<Void>> handleBiz(BusinessException ex) {
    ErrorCode ec = ex.getErrorCode();
    log.error("BusinessException: {}", ec.getCode(), ex);
    return ResponseEntity
        .status(ec.getStatus())
        .body(BaseResponse.error(ec.getCode(), ec.getMessage()));
  }

  // 입력 검증 예외
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
    String msg = ex.getBindingResult()
        .getAllErrors().stream()
        .findFirst()
        .map(ObjectError::getDefaultMessage)
        .orElse("입력 값이 올바르지 않습니다.");
    return ResponseEntity
        .badRequest()
        .body(BaseResponse.error("VALIDATION_001", msg));
  }

  // 외부 API 호출 실패
  @ExceptionHandler(RestClientException.class)
  public ResponseEntity<BaseResponse<Void>> handleClient(RestClientException ex) {
    log.error("External API error", ex);
    return ResponseEntity
        .status(502)
        .body(BaseResponse.error("EXTERNAL_API_ERROR", "외부 서비스 호출에 실패했습니다."));
  }

  // DB 처리 오류
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<BaseResponse<Void>> handleDb(DataAccessException ex) {
    log.error("DB error", ex);
    return ResponseEntity
        .status(500)
        .body(BaseResponse.error("DB_ERROR", "데이터 처리 중 오류가 발생했습니다."));
  }

  // 그 외
  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseResponse<Void>> handleUnknown(Exception ex) {
    log.error("Unknown error", ex);
    return ResponseEntity
        .status(500)
        .body(BaseResponse.error("UNKNOWN_ERROR", "서버 내부 오류가 발생했습니다."));
  }
}
