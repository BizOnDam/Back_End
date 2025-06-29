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
import io.jsonwebtoken.Claims;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
    return issueTokens(user);
  }

  // 리프레시 토큰 재발급(토큰 로테이션)
  public LoginResponse reissueRefreshTokens(Long userId, String refreshToken) {
    // 1. 리프레시 토큰 유효성 검증
    if (!validateRefreshToken(refreshToken)) {
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

  // 액세스 토큰만 재발급 (리프레시 토큰은 기존 것 사용)
  public LoginResponse reissueAccessToken(String refreshToken) {
    String userId = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
        .getRequest().getHeader("X-User-Id");

    if (!validateRefreshToken(refreshToken)) {
      throw new CustomException(AuthErrorCode.REFRESH_TOKEN_REQUIRED);
    }

    User user = authMapper.findByUserId(Long.valueOf(userId));
    if (user == null) {
      throw new CustomException(AuthErrorCode.USER_INFO_FAIL);
    }

    return issueAccessToken(user, refreshToken);
  }

  // 리프레시 토큰 검증 로직
  public boolean validateRefreshToken(String refreshToken) {
    String encrypted = encryptToken(refreshToken);
    try {
      Claims claims = jwtProvider.getClaims(encrypted);
      String tokenId = claims.getId();

      return refreshTokenMapper.findByUserIdAndTokenId(tokenId)
          .filter(rt -> rt.getRefreshToken().equals(encrypted))
          .isPresent();
    } catch (CustomException e) {
      return false;
    }
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
    claims.put("role", user.getRoleInCompany().name());

    // AccessToken, RefreshToken 발급
    String accessToken = jwtProvider.createAccessToken(user.getLoginId(), claims);
    String refreshTokenId = UUID.randomUUID().toString();
    String refreshToken = jwtProvider.createRefreshToken(user.getLoginId(), refreshTokenId);
    String encryptedRefreshToken = encryptToken(refreshToken);

    // 만료 시간 계산
    LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(jwtProvider.getRefreshTokenExpireTime() / 1000);

    // DB에 RefreshToken 저장(기존 값 덮어쓰기)
    try {
      refreshTokenMapper.insertOrUpdateRefreshToken(
          user.getUserId(),
          refreshTokenId,
          encryptedRefreshToken,
          expiresAt
      );
    } catch (Exception e) {
      // 토큰 저장 실패 시 TOKEN_FAIL 예외 처리
      throw new CustomException(AuthErrorCode.TOKEN_FAIL);
    }

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

  private LoginResponse issueAccessToken(User user, String refreshToken) {
    Company company = companyMapper.findByCompanyId(user.getCompanyId());
    String companyNameKr = (company != null) ? company.getCompanyNameKr() : null;

    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", user.getUserId());
    claims.put("role", user.getRoleInCompany().name());

    String accessToken = jwtProvider.createAccessToken(user.getLoginId(), claims);
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

  private String encryptToken(String token) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] encodedHash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(encodedHash);
    } catch (Exception e) {
      throw new CustomException(AuthErrorCode.TOKEN_FAIL); // 적절한 에러 코드 사용
    }
  }
}