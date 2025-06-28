package com.bizondam.company_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUser {
  private Long userId;
  private String nameKr;
  private String email;
  private String department;
  private String position;
  private String roleDesc;
  private String roleInCompany;
  private boolean isDeleted;

  public static CompanyUser fromEntity(CompanyUserEntity user) {
    return CompanyUser.builder()
        .userId(user.getUserId())
        .nameKr(user.getNameKr())
        .email(user.getEmail())
        .department(user.getDepartment())
        .position(user.getPosition())
        .roleDesc(user.getRoleDesc())
        .roleInCompany(user.getRoleInCompany())
        .isDeleted(user.isDeleted())
        .build();
  }
}