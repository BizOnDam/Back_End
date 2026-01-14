package com.bizondam.userservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;

public enum RoleInCompany {
  @Schema(description = "최초 가입자")
  CEO,
  @Schema(description = "직원")
  STAFF;
}
