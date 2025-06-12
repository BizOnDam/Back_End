package com.bizondam.userservice.dto.request;

import jakarta.validation.constraints.Email;
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
public class EmailSendRequest {
  @Email(message = "올바른 이메일 형식이어야 합니다.")
  @NotBlank(message = "이메일은 필수 입력값입니다.")
  private String email;
}
