package com.bizondam.userservice.mapper;

import com.bizondam.userservice.entity.EmailAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmailAuthMapper {
  EmailAuth findByEmailAndCode(@Param("email") String email, @Param("code") String code);
  void deleteByEmail(@Param("email") String email);
  void insertEmailAuth(EmailAuth emailAuth);
  boolean existsByEmail(@Param("email") String email);
}