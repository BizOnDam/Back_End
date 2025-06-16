package com.bizondam.userservice.mapper;

import java.time.LocalDateTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RefreshTokenMapper {
  void insertOrUpdateRefreshToken(@Param("userId") Long userId,
      @Param("tokenId") String tokenId,
      @Param("refreshToken") String refreshToken,
      @Param("expiresAt") LocalDateTime expiresAt);

  String findRefreshTokenByUserIdAndTokenId(@Param("userId") Long userId, @Param("tokenId") String tokenId);

  void deleteRefreshToken(@Param("userId") Long userId, @Param("tokenId") String tokenId);
}