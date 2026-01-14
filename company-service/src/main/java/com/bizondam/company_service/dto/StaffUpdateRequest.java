package com.bizondam.company_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffUpdateRequest {
  @Schema(description = "부서", example = "영업부")
  private String department;

  @Schema(description = "직책", example = "과장")
  private String position;

  @Schema(description = "담당 업무", example = "B2B 견적 응답 및 계약 관리")
  private String roleDesc;
}
