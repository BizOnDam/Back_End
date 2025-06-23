package com.bizondam.userservice.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshToken {
  private Long userId;
  private String tokenId;
  private String refreshToken;
  private LocalDateTime expiresAt;
}