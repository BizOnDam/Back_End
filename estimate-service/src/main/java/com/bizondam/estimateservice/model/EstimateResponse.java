package com.bizondam.estimateservice.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EstimateResponse {
  private Long responseId;       // response_id (PK, AUTO_INCREMENT)
  private Long requestId;        // request_id (FK → estimate_request.request_id)
  private Long supplierUserId;   // supplier_user_id (FK → users.user_id)
  private Long totalPrice;       // total_price (INT)
  private Integer status;        // status (1: 수신, 2: 발신, 3: 계약체결, 4: 계약미체결)
  private String paymentTerms;   // payment_terms (VARCHAR(255))
  private String warranty;       // warranty (VARCHAR(100))
  private String specialTerms;   // special_terms (TEXT)
  private LocalDateTime createdAt; // created_at (DATETIME)
}