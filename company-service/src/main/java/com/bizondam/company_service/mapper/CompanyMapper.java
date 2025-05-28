package com.bizondam.company_service.mapper;

import com.bizondam.company_service.entity.Company;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CompanyMapper {
  @Select("SELECT * FROM companies WHERE business_number = #{businessNumber}")
  Company selectByBusinessNumber(@Param("businessNumber") String businessNumber);
  int insertCompany(Company company);
  Company selectCompanyById(Long companyId);
  List<Company> selectAllCompanies();
  int updateCompany(Company company);
  int deleteCompany(Long companyId);
}