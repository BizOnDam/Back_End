package com.bizondam.userservice.mapper;

import com.bizondam.userservice.entity.MyPageUserInfo;
import com.bizondam.userservice.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
  //회사별 사용자 수 조회
  int countByCompanyId(@Param("companyId") Long companyId);
  //신규 사용자 등록
  int insertUser(User user);
  //기존 사용자 조회
  int countByLoginId(@Param("loginId") String loginId);
  //휴대폰 번호로 사용자 조회
  boolean existsByPhoneNumber(@Param("phoneNumber") String phoneNumber);
  //사용자 정보 조회
  MyPageUserInfo getMyPageUserInfo(@Param("userId") Long userId);
  // 비밀번호 변경
  void updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);
  // 로그인 아이디로 사용자
  User findByLoginId(@Param("loginId") String loginId);
}