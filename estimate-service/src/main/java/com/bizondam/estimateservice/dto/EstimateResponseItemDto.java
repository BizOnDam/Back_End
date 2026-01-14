package com.bizondam.estimateservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EstimateResponseItemDto {
  private Long responseItemId;         // (조회용/수정용) 자동생성 항목
  private Long itemId;                 // 견적 요청 품목 ID (estimate_request_item.item_id)
  private Integer unitPrice;           // 제안 단가
  private Integer deliveryDays;        // 납품 소요일
}