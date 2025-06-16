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
      @Value("${jwt.refreshTokenExpireTime}") long refreshTokenExpireTime
  ) {
    byte[] keyBytes = Base64.getDecoder().decode(secretKeyString);
    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    this.accessTokenExpireTime = accessTokenExpireTime;
    this.refreshTokenExpireTime = refreshTokenExpireTime;
  }

  // AccessToken 생성
  public String createAccessToken(String subject, Map<String, Object> claims) {
    Date now = new Date();
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
    } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
      throw new CustomException(AuthErrorCode.INVALID_ACCESS_TOKEN);
    }
  }

  // 토큰에서 jti 추출 (리프레시 토큰 관리용)
  public String extractTokenId(String token) {
    return getClaims(token).getId();
  }
}