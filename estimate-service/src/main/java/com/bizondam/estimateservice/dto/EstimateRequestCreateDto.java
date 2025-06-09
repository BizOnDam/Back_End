package com.bizondam.estimateservice.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EstimateRequestCreateDto {
  private Long requestId;
  private Long buyerUserId;            // 요청자 user_id
  private Long buyerCompanyId;         // 요청 회사 company_id
  private String detail;               // 요청 상세 설명
  private LocalDate dueDate;           // 납품 기한 (yyyy-MM-dd)
  private List<EstimateRequestItemDto> items;
}
