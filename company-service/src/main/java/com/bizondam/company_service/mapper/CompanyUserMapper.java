package com.bizondam.company_service.mapper;

import com.bizondam.company_service.entity.CompanyUserEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CompanyUserMapper {
  // 회사 내 전체 직원 조회
  List<CompanyUserEntity> selectUsersByCompany(@Param("companyId") Long companyId);
  // 특정 직원 조회
  CompanyUserEntity selectUserByIdAndCompany(@Param("userId") Long userId, @Param("companyId") Long companyId);
  // 직원 소프트 삭제 처리
  void deleteStaff(@Param("userId") Long userId);
  // 직원 정보 수정
  void updateStaffInfo(@Param("userId") Long userId,
      @Param("department") String department,
      @Param("position") String position,
      @Param("roleDesc") String roleDesc,
      @Param("updatedAt") LocalDateTime updatedAt);
  // CEO, STAFF 역할 업데이트
  void updateRoleInCompany(@Param("userId") Long userId,
      @Param("roleInCompany") String roleInCompany,
      @Param("updatedAt") LocalDateTime updatedAt);
}
