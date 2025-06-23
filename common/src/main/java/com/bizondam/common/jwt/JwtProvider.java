package com.bizondam.common.jwt;

import com.bizondam.common.exception.AuthErrorCode;
import com.bizondam.common.exception.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {
  private final Key secretKey;
  @Getter
  private final long accessTokenExpireTime;
  @Getter
  private final long refreshTokenExpireTime;

  public JwtProvider(
      @Value("${jwt.secretKey}") String secretKeyString,
      @Value("${jwt.accessTokenExpireTime}") long accessTokenExpireTime,
      @Value("${jwt.refreshTokenExpireTime}") long refreshTokenExpireTime) {
    byte[] keyBytes = Base64.getDecoder().decode(secretKeyString);
    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    this.accessTokenExpireTime = accessTokenExpireTime;
    this.refreshTokenExpireTime = refreshTokenExpireTime;
    log.error("secretKey(raw): {}", secretKeyString);
    log.error("secretKey(decoded): {}", Base64.getDecoder().decode(secretKeyString));
  }

  // AccessToken 생성
  public String createAccessToken(String subject, Map<String, Object> claims) {
    Date now = new Date();
    log.warn("AccessToken 생성: 서명에 사용된 키 = {}", Base64.getEncoder().encodeToString(secretKey.getEncoded()));
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + accessTokenExpireTime))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  // RefreshToken 생성
  public String createRefreshToken(String subject, String tokenId) {
    Date now = new Date();
    return Jwts.builder()
        .setSubject(subject)
        .setId(tokenId)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + refreshTokenExpireTime))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  // 토큰 검증 및 Claims 추출
  public Claims getClaims(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      throw new CustomException(AuthErrorCode.JWT_TOKEN_EXPIRED);
    } catch (UnsupportedJwtException e) {
      throw new CustomException(AuthErrorCode.UNSUPPORTED_TOKEN);
    } catch (MalformedJwtException e) {
      throw new CustomException(AuthErrorCode.MALFORMED_JWT_TOKEN);
    } catch (SignatureException e) {
      throw new CustomException(AuthErrorCode.INVALID_SIGNATURE);
    } catch (IllegalArgumentException e) {
      throw new CustomException(AuthErrorCode.ILLEGAL_ARGUMENT);
    }
  }

  // 토큰에서 jti 추출 (리프레시 토큰 관리용)
  public String extractTokenId(String token) {
    return getClaims(token).getId();
  }

  // signature만 검증하는 메서드
  public Claims getClaimsIgnoreExpiration(String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(secretKey)
          .build()
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      // signature는 유효했으니 claim은 꺼낼 수 있음
      return e.getClaims();
    }
  }
}