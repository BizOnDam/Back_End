package com.bizondam.userservice.mapper;

import com.bizondam.userservice.entity.RefreshToken;
import com.bizondam.userservice.entity.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthMapper {
  //로그인 아이디로 사용자 조회
  User findByLoginId(@Param("loginId") String loginId);
  // 사용자 ID로 사용자 조회 (reissueTokens 등에서 필요)
  User findByUserId(Long userId);

  Optional<RefreshToken> findByUserIdAndTokenId(
      @Param("userId") Long userId,
      @Param("tokenId") String tokenId
  );
}
