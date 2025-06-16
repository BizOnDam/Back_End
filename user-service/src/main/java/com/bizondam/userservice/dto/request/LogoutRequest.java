package com.bizondam.userservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequest {
  private Long userId;
  private String refreshToken;
}