package com.bizondam.userservice.mapper;

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
}