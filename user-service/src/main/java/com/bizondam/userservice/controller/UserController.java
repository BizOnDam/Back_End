package com.bizondam.userservice.controller;

import com.bizondam.common.exception.CustomException;
import com.bizondam.common.response.BaseResponse;
import com.bizondam.userservice.dto.request.EmailSendRequest;
import com.bizondam.userservice.dto.request.EmailVerifyRequest;
import com.bizondam.userservice.dto.request.SignUpRequest;
import com.bizondam.userservice.dto.response.SignUpResponse;
import com.bizondam.userservice.exception.UserErrorCode;
import com.bizondam.userservice.service.EmailAuthService;
import com.bizondam.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "User 관리 API")
public class UserController {
  private final UserService userService;
  private final EmailAuthService emailAuthService;

  @Operation(summary = "회원가입", description = "새로운 유저 등록: 회사 내 최초 가입자는 CEO, 이후 가입자는 STAFF")
  @PostMapping("/register")
  public ResponseEntity<BaseResponse<SignUpResponse>> register(@RequestBody @Validated SignUpRequest request) {
    if (!emailAuthService.isVerified(request.getEmail(), request.getCode())) {
      throw new CustomException(UserErrorCode.EMAIL_NOT_VERIFIED);
    }
    SignUpResponse resp = userService.registerUser(request);
    return ResponseEntity.ok(BaseResponse.success("회원가입에 성공했습니다.", resp));
  }

  @Operation(summary = "아이디 중복 검사", description = "같은 아이디로 가입 불가능")
  @GetMapping("/check-login-id")
  public ResponseEntity<BaseResponse<Boolean>> checkLoginId(@RequestParam String loginId) {
    boolean isDuplicate = userService.isLoginIdDuplicate(loginId);
    if (isDuplicate) {
      return ResponseEntity.ok(BaseResponse.success("이미 사용 중인 아이디입니다.", true));
    } else {
      return ResponseEntity.ok(BaseResponse.success("사용 가능한 아이디입니다.", false));
    }
  }

  @Operation(summary = "이메일 인증(유효기간 10분)", description = "이메일로 랜덤 6자리 인증번호 발송")
  @PostMapping("/email-auth")
  public ResponseEntity<BaseResponse<Void>> sendEmailAuthCode(@RequestBody EmailSendRequest request) {
    emailAuthService.createAndSendAuthCode(request.getEmail());
    return ResponseEntity.ok(BaseResponse.success("인증코드가 발송되었습니다.", null));
  }

  @Operation(summary = "인증번호 검증", description = "DB에 존재하는 인증번호와 일치하는지 검사")
  @PostMapping("/email-auth/verify")
  public ResponseEntity<BaseResponse<Boolean>> verifyEmailAuthCode(@RequestBody EmailVerifyRequest request) {
    boolean verified = emailAuthService.isVerified(request.getEmail(), request.getCode());
    if (verified) {
      return ResponseEntity.ok(BaseResponse.success("이메일 인증 성공", true));
    } else {
      return ResponseEntity.badRequest().body(BaseResponse.error(400, "인증코드가 일치하지 않거나 만료되었습니다."));
    }
  }
}

