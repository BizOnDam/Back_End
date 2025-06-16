package com.bizondam.userservice.service;

import com.bizondam.common.exception.AuthErrorCode;
import com.bizondam.common.exception.CustomException;
import com.bizondam.common.jwt.JwtProvider;
import com.bizondam.userservice.dto.response.LoginResponse;
import com.bizondam.userservice.entity.Company;
import com.bizondam.userservice.entity.User;
import com.bizondam.userservice.mapper.AuthMapper;
import com.bizondam.userservice.mapper.CompanyMapper;
import com.bizondam.userservice.mapper.RefreshTokenMapper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final AuthMapper authMapper;
  private final CompanyMapper companyMapper;
  private final RefreshTokenMapper refreshTokenMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  public AuthService(
      AuthMapper authMapper, CompanyMapper companyMapper,
      RefreshTokenMapper refreshTokenMapper,
      PasswordEncoder passwordEncoder,
      JwtProvider jwtProvider
  ) {
    this.companyMapper = companyMapper;
    this.authMapper = authMapper;
    this.refreshTokenMapper = refreshTokenMapper;
    this.passwordEncoder = passwordEncoder;
    this.jwtProvider = jwtProvider;
  }

  // 로그인: 사용자 검증 후 토큰 발급 및 저장
  public LoginResponse login(String loginId, String loginPwd) {
    User user = authMapper.findByLoginId(loginId);
    if (user == null || !passwordEncoder.matches(loginPwd, user.getLoginPwd())) {
      throw new CustomException(AuthErrorCode.LOGIN_FAIL);
    }
    if (!Boolean.TRUE.equals(user.getIsVerified())) {
      throw new CustomException(AuthErrorCode.USER_INFO_FAIL);
    }
    return issueTokens(user);
  }

  // 리프레시 토큰 재발급(토큰 로테이션)
  public LoginResponse reissueTokens(Long userId, String refreshToken) {
    // 1. 리프레시 토큰 유효성 검증
    if (!validateRefreshToken(userId, refreshToken)) {
      throw new CustomException(AuthErrorCode.REFRESH_TOKEN_REQUIRED);
    }
    // 2. 사용자 정보 조회
    User user = authMapper.findByUserId(userId);
    if (user == null) {
      throw new CustomException(AuthErrorCode.USER_INFO_FAIL);
    }
    // 3. 새 토큰 발급 및 저장(기존 refreshToken 무효화)
    return issueTokens(user);
  }

  // 리프레시 토큰 유효성 검증
  public boolean validateRefreshToken(Long userId, String refreshToken) {
    String tokenId = jwtProvider.extractTokenId(refreshToken);
    String savedToken = refreshTokenMapper.findRefreshTokenByUserIdAndTokenId(userId, tokenId);
    return savedToken != null && savedToken.equals(refreshToken);
  }

  // 로그아웃(리프레시 토큰 폐기)
  public void logout(Long userId, String refreshToken) {
    String tokenId = jwtProvider.extractTokenId(refreshToken);
    refreshTokenMapper.deleteRefreshToken(userId, tokenId);
  }

  // --- 공통 토큰 발급/저장 로직 ---
  private LoginResponse issueTokens(User user) {
    // 회사 정보 조회
    Company company = companyMapper.findByCompanyId(user.getCompanyId());
    String companyNameKr = (company != null) ? company.getCompanyNameKr() : null;

    // JWT claims 생성
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", user.getUserId());
    claims.put("loginId", user.getLoginId());
    claims.put("role", user.getRoleInCompany().name());

    // AccessToken, RefreshToken 발급
    String accessToken = jwtProvider.createAccessToken(user.getLoginId(), claims);
    String refreshTokenId = UUID.randomUUID().toString();
    String refreshToken = jwtProvider.createRefreshToken(user.getLoginId(), refreshTokenId);

    // 만료 시간 계산
    LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(jwtProvider.getRefreshTokenExpireTime() / 1000);

    // DB에 RefreshToken 저장(기존 값 덮어쓰기)
    refreshTokenMapper.insertOrUpdateRefreshToken(
        user.getUserId(),
        refreshTokenId,
        refreshToken,
        expiresAt
    );

    long expirationTime = System.currentTimeMillis() + jwtProvider.getAccessTokenExpireTime();

    return LoginResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .userId(user.getUserId())
        .loginId(user.getLoginId())
        .username(user.getNameKr())
        .roleInCompany(user.getRoleInCompany())
        .companyId(String.valueOf(user.getCompanyId()))
        .companyNameKr(companyNameKr)
        .expirationTime(expirationTime)
        .build();
  }
}