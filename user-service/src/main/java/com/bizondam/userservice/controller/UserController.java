package com.bizondam.userservice.controller;

import com.bizondam.common.response.BaseResponse;
import com.bizondam.userservice.dto.request.SignUpRequest;
import com.bizondam.userservice.dto.response.SignUpResponse;
import com.bizondam.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User", description = "User 관리 API")
public class UserController {
  private final UserService userService;

  @Operation(summary = "회원가입", description = "새로운 유저 등록: 회사 내 최초 가입자는 CEO, 이후 가입자는 STAFF")
  @PostMapping("/register")
  public ResponseEntity<BaseResponse<SignUpResponse>> register(
      @RequestBody @Validated SignUpRequest request) {
    SignUpResponse resp = userService.registerUser(request);
    return ResponseEntity.ok(BaseResponse.success("회원가입에 성공했습니다.", resp));
  }
}

