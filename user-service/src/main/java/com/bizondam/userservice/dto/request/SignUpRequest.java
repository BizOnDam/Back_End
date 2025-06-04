package com.bizondam.userservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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
@Schema(title = "SignUpRequest DTO", description = "사용자 회원가입을 위한 데이터 전송")
public class SignUpRequest {
  @NotNull
  private Long companyId;

  @Email @NotBlank(message = "이메일 항목은 필수입니다.") @Size(max = 100)
  private String email;

  @NotBlank(message = "사용자 아이디 항목은 필수입니다.") @Size(max = 50)
  private String loginId;

  @NotBlank(message = "사용자 비밀번호 항목은 필수입니다.") @Size(min = 8, max = 255)
  private String loginPwd;

  @NotBlank(message = "사용자 이름 항목은 필수입니다.") @Size(max = 50)
  private String nameKr;

  @Size(max = 50)
  private String nameEn;

  @Size(max = 100)
  private String department;

  @Size(max = 100)
  private String position;

  private String roleDesc;

  @NotBlank(message = "사용자 연락처 항목은 필수입니다.") @Size(max = 15)
  private String phoneNumber;

  @NotNull
  private String authProvider; // PASS or EMAIL
}
