package com.bizondam.userservice.service;

import com.bizondam.userservice.dto.request.SignUpRequest;
import com.bizondam.userservice.dto.response.SignUpResponse;
import com.bizondam.userservice.entity.RoleInCompany;
import com.bizondam.userservice.entity.User;
import com.bizondam.userservice.mapper.UserMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public SignUpResponse registerUser(SignUpRequest signupRequest) {
    // 0) 휴대폰 번호 중복 검사
    if (userMapper.existsByPhoneNumber(signupRequest.getPhoneNumber())) {
      throw new IllegalArgumentException("이미 가입된 휴대폰 번호입니다.");
    }
    // 1) 비밀번호 암호화
    String encodedPwd = passwordEncoder.encode(signupRequest.getLoginPwd());

    // 2) 회사 내 기존 유저 수로 역할 결정
    int count = userMapper.countByCompanyId(signupRequest.getCompanyId());
    RoleInCompany role = (count == 0) ? RoleInCompany.CEO : RoleInCompany.STAFF;

    // 3) 엔티티에 값 세팅
    User user = new User();
    user.setCompanyId(signupRequest.getCompanyId());
    user.setEmail(signupRequest.getEmail());
    user.setLoginId(signupRequest.getLoginId());
    user.setLoginPwd(encodedPwd);
    user.setNameKr(signupRequest.getNameKr());
    user.setNameEn(signupRequest.getNameEn());
    user.setDepartment(signupRequest.getDepartment());
    user.setPosition(signupRequest.getPosition());
    user.setRoleDesc(signupRequest.getRoleDesc());
    user.setPhoneNumber(signupRequest.getPhoneNumber());
    user.setRoleInCompany(role);
    user.setAuthProvider("EMAIL");
    user.setIsVerified(true); // 이메일 인증 검증이 끝났으므로 true
    user.setCreatedAt(LocalDateTime.now());

    // 4) DB 저장
    userMapper.insertUser(user);

    // 5) DTO 변환
    SignUpResponse response = convertToSignUpResponse(user);

    // 6) 로깅
    log.debug("Registered new user [id={}]", user.getUserId());
    log.info("New user registered - id: {}, role: {}", user.getUserId(), user.getRoleInCompany());

    // 7) 변환된 DTO 반환
    return response;
  }

  private SignUpResponse convertToSignUpResponse(User user) {
    return SignUpResponse.builder()
        .userId(user.getUserId())
        .companyId(user.getCompanyId())
        .email(user.getEmail())
        .loginId(user.getLoginId())
        .nameKr(user.getNameKr())
        .nameEn(user.getNameEn())
        .department(user.getDepartment())
        .position(user.getPosition())
        .roleDesc(user.getRoleDesc())
        .phoneNumber(user.getPhoneNumber())
        .roleInCompany(user.getRoleInCompany())
        .isVerified(user.getIsVerified())
        .createdAt(user.getCreatedAt())
        .build();
  }

  // 아이디 중복 검사
  public boolean isLoginIdDuplicate(String loginId) {
    int count = userMapper.countByLoginId(loginId);
    return count > 0;
  }
}