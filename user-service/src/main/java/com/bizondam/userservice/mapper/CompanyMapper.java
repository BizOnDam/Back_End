package com.bizondam.userservice.mapper;

import com.bizondam.userservice.entity.Company;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyMapper {
  Company findByCompanyId(Long companyId);
}