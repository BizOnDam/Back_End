package com.bizondam.userservice.exception;

import com.bizondam.common.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {
  USERNAME_ALREADY_EXIST("USER_4001", "이미 존재하는 사용자 아이디입니다.", HttpStatus.BAD_REQUEST),
  USER_NOT_FOUND("USER_4002", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
  EMAIL_NOT_VERIFIED("USER_4003", "이메일 인증이 완료되지 않았습니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}