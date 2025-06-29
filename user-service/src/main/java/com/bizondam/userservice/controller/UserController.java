package com.bizondam.userservice.controller;

import com.bizondam.common.exception.CustomException;
import com.bizondam.common.response.BaseResponse;
import com.bizondam.userservice.dto.request.*;
import com.bizondam.userservice.entity.MyPageUserInfo;
import com.bizondam.userservice.dto.response.SignUpResponse;
import com.bizondam.userservice.entity.User;
import com.bizondam.userservice.exception.UserErrorCode;
import com.bizondam.userservice.mapper.UserMapper;
import com.bizondam.userservice.service.EmailAuthService;
import com.bizondam.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "User 관리 API")
public class UserController {
  private final UserService userService;
  private final EmailAuthService emailAuthService;
  private final UserMapper userMapper;

  @Operation(summary = "회원가입", description = "새로운 유저 등록: 회사 내 최초 가입자는 CEO, 이후 가입자는 STAFF")
  @PostMapping("/register")
  public ResponseEntity<BaseResponse<SignUpResponse>> register(@RequestBody @Validated SignUpRequest request) {
    try {
      if (!emailAuthService.isVerified(request.getEmail(), request.getCode())) {
        throw new CustomException(UserErrorCode.EMAIL_NOT_VERIFIED);
      }
      SignUpResponse resp = userService.registerUser(request);
      return ResponseEntity.ok(BaseResponse.success("회원가입에 성공했습니다.", resp));
    } catch (IllegalArgumentException e) {
      // 휴대폰 번호 중복 예외 처리
      return ResponseEntity.badRequest().body(BaseResponse.error(400, e.getMessage()));
    }
  }

  @Operation(summary = "아이디 중복 검사", description = "같은 아이디로 가입 불가능")
  @GetMapping("/check-login-id")
  public ResponseEntity<BaseResponse<Boolean>> checkLoginId(@RequestParam String loginId) {
    boolean isDuplicate = userService.isLoginIdDuplicate(loginId);
    if (isDuplicate) {
      return ResponseEntity
          .status(HttpStatus.CONFLICT)
          .body(BaseResponse.fail("이미 사용 중인 아이디입니다.", true));
    } else {
      return ResponseEntity
          .ok(BaseResponse.success("사용 가능한 아이디입니다.", false));
    }
  }

  @Operation(summary = "이메일 인증 - 회원가입", description = "이메일로 랜덤 6자리 인증번호 발송(유효기간 10분)")
  @PostMapping("/email-auth/signup")
  public ResponseEntity<BaseResponse<Void>> sendEmailForSignup(@RequestBody EmailSendRequest request) {
    emailAuthService.createAndSendAuthCodeForSignup(request.getEmail());
    return ResponseEntity.ok(BaseResponse.success("인증코드가 발송되었습니다.", null));
  }

  @Operation(summary = "이메일 인증 - 아이디 찾기", description = "이메일로 랜덤 6자리 인증번호 발송(유효기간 10분)")
  @PostMapping("/email-auth/find-id")
  public ResponseEntity<BaseResponse<Void>> sendEmailForFindId(@RequestBody EmailSendRequest request) {
    emailAuthService.createAndSendAuthCodeForFindId(request.getEmail());
    return ResponseEntity.ok(BaseResponse.success("인증코드가 발송되었습니다.", null));
  }

  @Operation(summary = "인증번호 검증", description = "DB에 존재하는 인증번호와 일치하는지 검사")
  @PostMapping("/email-auth/verify")
  public ResponseEntity<BaseResponse<Boolean>> verifyEmailAuthCode(@RequestBody EmailVerifyRequest request) {
    boolean verified = emailAuthService.isVerified(request.getEmail(), request.getCode());
    if (verified) {
      return ResponseEntity.ok(BaseResponse.success("이메일 인증 성공", true));
    } else {
      return ResponseEntity .badRequest().body(BaseResponse.fail("인증코드가 일치하지 않거나 만료되었습니다.", false));
    }
  }

  @Operation(summary = "마이페이지 사용자 정보 조회", description = "자기 자신의 정보 조회")
  @GetMapping("/mypage-info")
  public ResponseEntity<BaseResponse<MyPageUserInfo>> getMyPageUserInfo(
      @RequestHeader("X-User-Id") Long userId
  ) {
    MyPageUserInfo result = userService.getMyPageUserInfo(userId);
    if (result == null) {
      return ResponseEntity.ok(BaseResponse.success("조회된 정보 없음", null));
    }
    return ResponseEntity.ok(BaseResponse.success("마이페이지 정보 조회 성공", result));
  }

  @Operation(summary = "비밀번호 변경", description = "로그인 되어있는 경우, 현재 비밀번호 필요")
  @PatchMapping("/update-password")
  public ResponseEntity<BaseResponse<Void>> updatePassword(
          @RequestHeader("X-User-Id") Long userId,
          @RequestBody PasswordUpdateRequest request
  ) {
    userService.updatePassword(userId, request);
    return ResponseEntity.ok(BaseResponse.success("비밀번호 변경 완료", null));
  }

  @Operation(summary = "아이디로 이메일 찾기", description = "비밀번호 재설정 시 사용")
  @GetMapping("/find-email")
  public ResponseEntity<BaseResponse<String>> findEmailByLoginId(@RequestParam String loginId) {
    User user = userMapper.findByLoginId(loginId);
    if (user == null) {
      return ResponseEntity.badRequest().body(BaseResponse.fail("존재하지 않는 아이디입니다.", null));
    }
    return ResponseEntity.ok(BaseResponse.success("이메일 조회 성공", user.getEmail()));
  }

  @Operation(summary = "비밀번호 재설정 - 이메일 인증 기반", description = "이메일 인증 완료된 사용자만 비밀번호 재설정 가능")
  @PatchMapping("/reset-password")
  public ResponseEntity<BaseResponse<Void>> resetPasswordFinal(@RequestBody PasswordResetFinalRequest request) {
    boolean verified = emailAuthService.isVerifiedAndMatch(request.getEmail(), request.getCode(), request.getLoginId());
    if (!verified) {
      return ResponseEntity.badRequest().body(BaseResponse.fail("인증 실패 또는 정보 불일치", null));
    }

    userService.resetPassword(request.getLoginId(), new PasswordResetRequest(request.getNewPassword()));
    return ResponseEntity.ok(BaseResponse.success("비밀번호 재설정 완료", null));
  }
}