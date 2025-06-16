package com.bizondam.userservice.dto.response;

import com.bizondam.userservice.entity.RoleInCompany;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "LoginResponse DTO", description = "사용자 로그인에 대한 응답 반환")
public class LoginResponse {
  @Schema(description = "사용자 Access Token")
  private String accessToken;

  @Schema(description = "사용자 Refresh Token")
  private final String refreshToken;

  @Schema(description = "사용자 ID", example = "1")
  private Long userId;

  @Schema(description = "사용자 로그인 ID", example = "kohyun0223")
  private String loginId;

  @Schema(description = "사용자 이름", example = "kohyun2002@naver.com")
  private String username;

  @Schema(description = "사용자 권한", example = "STAFF")
  private RoleInCompany roleInCompany;

  @Schema(description = "회사 ID", example = "679")
  private String companyId;

  @Schema(description = "회사명", example = "릴즈")
  private String companyNameKr;

  @Schema(description = "사용자 Access Token 만료 시간", example = "1800000")
  private Long expirationTime;
}