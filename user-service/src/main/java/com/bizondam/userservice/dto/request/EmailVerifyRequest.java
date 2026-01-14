package com.bizondam.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerifyRequest {
  @NotBlank(message = "이메일은 필수 입력값입니다.")
  private String email;

  @NotBlank(message = "인증 코드는 필수 입력값입니다.")
  private String code;
}