package com.bizondam.userservice.dto.request;

import lombok.Getter;

@Getter
public class PasswordResetFinalRequest {
  private String loginId;
  private String email;
  private String code;
  private String newPassword;
}