package com.bizondam.userservice.controller;

import com.bizondam.common.exception.CustomException;
import com.bizondam.common.response.BaseResponse;
import com.bizondam.userservice.dto.request.LoginRequest;
import com.bizondam.userservice.dto.request.LogoutRequest;
import com.bizondam.userservice.entity.RefreshToken;
import com.bizondam.userservice.dto.response.LoginResponse;
import com.bizondam.userservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "로그인 및 회원 정보 관련 API")
public class AuthController {
  private final AuthService authService;

  @Operation(summary = "로그인", description = "아이디, 비밀번호 일치 여부 확인/JWT 생성 및 반환")
  @PostMapping("/login")
  public ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
    try {
      LoginResponse loginResponse = authService.login(request.getLoginId(), request.getLoginPwd());
      return ResponseEntity.ok(BaseResponse.success("로그인 성공", loginResponse));
    } catch (CustomException e) {
      // 예: 아이디/비밀번호 불일치, 계정 정지 등
      return ResponseEntity
          .status(e.getErrorCode().getStatus()) // 예: 401 Unauthorized
          .body(BaseResponse.fail(e.getMessage(), null));
    }
  }

  @Operation(summary = "access 토큰 재발급", description = "access 토큰 재발급(refresh 토큰 이용)")
  @PostMapping("/reissue-access-token")
  public ResponseEntity<BaseResponse<LoginResponse>> reissueAccessToken(
      @RequestParam Long userId, @RequestParam String refreshToken) {
    try {
      LoginResponse response = authService.reissueAccessToken(userId, refreshToken);
      return ResponseEntity.ok(BaseResponse.success("Access 토큰 재발급 성공", response));
    } catch (CustomException e) {
      // 예: 토큰 만료, 위조, 매칭 실패 등
      return ResponseEntity
          .status(e.getErrorCode().getStatus()) // 예: 401 Unauthorized, 400 Bad Request 등
          .body(BaseResponse.fail(e.getMessage(), null));
    }
  }

  @Operation(summary = "refresh && access 토큰 재발급", description = "access, refresh 토큰 모두 재발급(refresh 토큰 만료 시)")
  @PostMapping("/reissue-refresh-token")
  public ResponseEntity<BaseResponse<LoginResponse>> reissueRefreshToken(@RequestBody RefreshToken request) {
    try {
      boolean isValid = authService.validateRefreshToken(request.getUserId(), request.getRefreshToken());
      if (!isValid) {
        // 예: 토큰 만료, 위조, 매칭 실패 등
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(BaseResponse.fail("Refresh 토큰이 유효하지 않습니다.", null));
      }
      LoginResponse response = authService.reissueRefreshTokens(request.getUserId(), request.getRefreshToken());
      return ResponseEntity.ok(BaseResponse.success("Refresh, Access 토큰 재발급 성공", response));
    } catch (CustomException e) {
      return ResponseEntity
          .status(e.getErrorCode().getStatus())
          .body(BaseResponse.fail(e.getMessage(), null));
    }
  }

  @Operation(summary = "로그아웃", description = "리프레시 토큰 폐기")
  @PostMapping("/logout")
  public ResponseEntity<BaseResponse<Void>> logout(@RequestBody LogoutRequest request) {
    log.info("Logout 실행");
    try {
      authService.logout(request.getUserId(), request.getRefreshToken());
      return ResponseEntity.ok(BaseResponse.success("로그아웃 성공", null));
    } catch (CustomException e) {
      // 예: 토큰이 유효하지 않음, 이미 만료됨 등
      return ResponseEntity
          .status(e.getErrorCode().getStatus()) // 예: 400, 401 등
          .body(BaseResponse.fail(e.getMessage(), null));
    }
  }
}