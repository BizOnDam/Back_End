package com.bizondam.estimateservice.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EstimateResponseCreateDto {
  private Long responseId;
  private Long requestId;              // 견적 요청서 ID (estimate_request.request_id)
  private Long supplierUserId;         // 응답자 user_id
  private Integer status;                 // 상태
  private Long totalPrice;             // 총 금액
  private String paymentTerms;         // 결제조건 (예: 계약금 30%, 잔금 70%)
  private String warranty;             // 보증 (예: 1년)
  private String specialTerms;         // 특별조항
  private List<EstimateResponseItemDto> responseItems;
}