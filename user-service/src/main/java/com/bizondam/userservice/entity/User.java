package com.bizondam.userservice.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
  private Long userId;
  private Long companyId;
  private String email;
  private String loginId;
  private String loginPwd;
  private String nameKr;
  private String nameEn;
  private String department;
  private String position;
  private String roleDesc;
  private String phoneNumber;
  private RoleInCompany roleInCompany;
  private Boolean isVerified;
  private String authProvider;  // PASS or EMAIL
  private LocalDateTime createdAt;
}
