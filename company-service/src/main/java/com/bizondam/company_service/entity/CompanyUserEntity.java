package com.bizondam.company_service.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompanyUserEntity {
  private Long userId;
  private Long companyId;
  private String email;
  private String loginId;
  private String nameKr;
  private String department;
  private String position;
  private String roleDesc;
  private String phoneNumber;
  private String roleInCompany;
  private boolean isDeleted;
}