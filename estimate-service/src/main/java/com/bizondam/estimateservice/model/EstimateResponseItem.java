package com.bizondam.estimateservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EstimateResponseItem {
  private Long responseItemId;  // response_item_id (PK, AUTO_INCREMENT)
  private Long responseId;      // response_id (FK → estimate_response.response_id)
  private Long itemId;          // item_id (FK → estimate_request_item.item_id)
  private Integer unitPrice;    // unit_price (INT)
  private Integer deliveryDays; // delivery_days (INT)
}