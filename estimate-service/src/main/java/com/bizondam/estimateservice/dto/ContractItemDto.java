package com.bizondam.estimateservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractItemDto {
  private Long requestId;

  // estimate_request_item
  private Long itemId;
  private Long productId;
  private String specification;
  private Integer quantity;

  // product_meta_batch
  private String categoryCode;
  private String categoryName;
  private String detailCategoryCode;
  private String detailCategoryName;

  // estimate_response_item
  private Long responseItemId;
  private Long responseId;
  private Integer unitPrice;
  private Integer deliveryDays;
}
