package com.bizondam.userservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageUserInfo {
  private String nameKr;
  private String nameEn;
  private String email;
  private String loginId;
  private String loginPwd;
  private String department;
  private String position;
  private String roleDesc;
  private String phoneNumber;
  private String companyNameKr;
}
