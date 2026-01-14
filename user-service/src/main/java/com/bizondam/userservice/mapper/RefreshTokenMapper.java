package com.bizondam.userservice.mapper;

import com.bizondam.userservice.entity.RefreshToken;
import java.time.LocalDateTime;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RefreshTokenMapper {
  Optional<RefreshToken> findByUserIdAndTokenId(
      @Param("tokenId") String tokenId
  );

  void insertOrUpdateRefreshToken(
      @Param("userId") Long userId,
      @Param("tokenId") String tokenId,
      @Param("refreshToken") String refreshToken,
      @Param("expiresAt") LocalDateTime expiresAt
  );

  void deleteRefreshToken(
      @Param("userId") Long userId,
      @Param("tokenId") String tokenId
  );
}