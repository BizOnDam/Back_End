package com.bizondam.userservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest {
  private Long userId;
  private String refreshToken;
}