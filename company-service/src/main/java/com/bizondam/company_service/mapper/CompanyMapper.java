package com.bizondam.company_service.mapper;

import com.bizondam.company_service.entity.Company;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CompanyMapper {
  // 사업자 등록 번호로 ㅔ회사 찾기
  Company selectByBusinessNumber(@Param("businessNumber") String businessNumber);
  // 회사 등록
  int insertCompany(Company company);
  // company_id로 회사 찾기
  Company findById(Long companyId);
}