package com.bizondam.company_service.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompanyUserEntity {
  private Long userId;
  private Long companyId;
  private String nameKr;
  private String email;
  private String phoneNumber;
  private String loginId;
  private String department;
  private String position;
  private String roleDesc;
  private String roleInCompany;
  private LocalDateTime createdAt;
  private boolean isDeleted;
}