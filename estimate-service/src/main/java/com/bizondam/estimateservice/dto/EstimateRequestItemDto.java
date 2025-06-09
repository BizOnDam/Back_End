package com.bizondam.estimateservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EstimateRequestItemDto {
  private Long itemId;            // (조회용/수정용) 자동생성 항목
  private Long   productId;       // DB에 맞추어 수정
  private String specification;   // 규격(예: 다스, 개 등)
  private Integer quantity;       // 요청 수량
}