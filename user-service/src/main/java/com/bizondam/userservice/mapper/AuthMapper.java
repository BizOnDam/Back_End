package com.bizondam.userservice.mapper;

import com.bizondam.userservice.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
  //로그인 아이디로 사용자 조회
  User findByLoginId(String loginId);
  // 사용자 ID로 사용자 조회 (reissueTokens 등에서 필요)
  User findByUserId(Long userId);
}
