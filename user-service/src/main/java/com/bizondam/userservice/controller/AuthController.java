package com.bizondam.userservice.controller;

import com.bizondam.common.exception.AuthErrorCode;
import com.bizondam.common.exception.CustomException;
import com.bizondam.common.response.BaseResponse;
import com.bizondam.userservice.dto.request.LoginRequest;
import com.bizondam.userservice.dto.request.LogoutRequest;
import com.bizondam.userservice.dto.request.RefreshTokenRequest;
import com.bizondam.userservice.dto.response.LoginResponse;
import com.bizondam.userservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "로그인 및 회원 정보 관련 API")
public class AuthController {
  private final AuthService authService;

  @Operation(summary = "로그인", description = "아이디, 비밀번호 일치 여부 확인/JWT 생성 및 반환")
  @PostMapping("/login")
  public ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
    LoginResponse loginResponse = authService.login(request.getLoginId(), request.getLoginPwd());
    return ResponseEntity.ok(BaseResponse.success("로그인 성공", loginResponse));
  }

  @Operation(summary = "토큰 재발급", description = "access, refresh 토큰 모두 재발급(refresh 토큰 만료 시)")
  @PostMapping("/refresh-token")
  public ResponseEntity<BaseResponse<LoginResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
    boolean isValid = authService.validateRefreshToken(request.getUserId(), request.getRefreshToken());
    if (!isValid) {
      throw new CustomException(AuthErrorCode.REFRESH_TOKEN_REQUIRED);
    }
    // 새 AccessToken, RefreshToken 발급 로직
    LoginResponse response = authService.reissueTokens(request.getUserId(), request.getRefreshToken());
    return ResponseEntity.ok(BaseResponse.success("토큰 재발급 성공", response));
  }

  @Operation(summary = "로그아웃", description = "리프레시 토큰 폐기")
  @PostMapping("/logout")
  public ResponseEntity<BaseResponse<Void>> logout(@RequestBody LogoutRequest request) {
    authService.logout(request.getUserId(), request.getRefreshToken());
    return ResponseEntity.ok(BaseResponse.success("로그아웃 성공", null));
  }
}
