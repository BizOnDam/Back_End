package com.bizondam.company_service.mapper;

import com.bizondam.company_service.entity.Company;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CompanyMapper {
  Company selectByBusinessNumber(@Param("businessNumber") String businessNumber);
  Company findByCompanyId(Long companyId);
  List<Company> selectAllCompanies();
  int insertCompany(Company company);
  int updateCompany(Company company);
  int deleteCompany(Long companyId);
}