package com.bizondam.userservice.dto.response;

import com.bizondam.userservice.entity.RoleInCompany;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SignUpResponse {
  private Long userId;
  private Long companyId;
  private String email;
  private String loginId;
  private String nameKr;
  private String nameEn;
  private String department;
  private String position;
  private String roleDesc;
  private String phoneNumber;
  private RoleInCompany roleInCompany;
  private Boolean isVerified;
  private LocalDateTime createdAt;
}