package com.bizondam.company_service.mapper;

import com.bizondam.company_service.domain.Company;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CompanyMapper {
  Company selectByBusinessNumber(String businessNumber);
  int insertCompany(Company company);
  Company selectCompanyById(Long companyId);
  List<Company> selectAllCompanies();
  int updateCompany(Company company);
  int deleteCompany(Long companyId);
}