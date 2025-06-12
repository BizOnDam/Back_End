package com.bizondam.userservice.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailAuth {
  private Long id;
  private String email;
  private String code;
  private LocalDateTime expiredAt;

  public boolean isExpired() {
    return expiredAt.isBefore(LocalDateTime.now());
  }
}
