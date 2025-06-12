package com.bizondam.userservice.service;

import com.bizondam.common.exception.CustomException;
import com.bizondam.userservice.exception.UserErrorCode;
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
    // 1) 기존 회원 중복 검사
    if (userMapper.existsByLoginId(signupRequest.getLoginId())) {
      throw new CustomException(UserErrorCode.USERNAME_ALREADY_EXIST);
    }
    // 2) 비밀번호 암호화
    String encodedPwd = passwordEncoder.encode(signupRequest.getLoginPwd());

    // 3) 회사 내 기존 유저 수로 역할 결정
    int count = userMapper.countByCompanyId(signupRequest.getCompanyId());
    RoleInCompany role = (count == 0) ? RoleInCompany.CEO : RoleInCompany.STAFF;

    // 4) 엔티티에 값 세팅
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
    user.setIsVerified(signupRequest.getIsVerified());
    user.setAuthProvider(signupRequest.getAuthProvider());
    user.setCreatedAt(LocalDateTime.now());

    // 5) DB 저장
    userMapper.insertUser(user);

    // 6) DTO 변환
    SignUpResponse response = convertToSignUpResponse(user);

    // 7) 로깅
    log.debug("Registered new user [id={}]", user.getUserId());
    log.info("New user registered - id: {}, role: {}", user.getUserId(), user.getRoleInCompany());

    // 8) 변환된 DTO 반환
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

}