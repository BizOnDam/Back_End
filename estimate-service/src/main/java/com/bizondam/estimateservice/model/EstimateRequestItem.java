package com.bizondam.estimateservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstimateRequestItem {
  private Long itemId;               // item_id (PK, AUTO_INCREMENT)
  private Long requestId;            // request_id (FK → estimate_request.request_id)
  private Long productId;            // product_meta_batch.product_id FK
  private String specification;      // specification (VARCHAR(255))
  private Integer quantity;          // quantity (INT)
}