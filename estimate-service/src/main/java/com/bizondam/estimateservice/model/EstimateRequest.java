package com.bizondam.estimateservice.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class EstimateRequest {
  private Long requestId;          // request_id (PK, AUTO_INCREMENT)
  private Long buyerUserId;        // buyer_user_id (FK → users.user_id)
  private Long buyerCompanyId;     // buyer_company_id (FK → companies.company_id)
  private Long supplierCompanyId;  // supplier_company_id (FK → companies.company_id), NULL 허용
  private Integer status;          // status (1: 수신, 2: 발신, 3: 계약체결, 4: 계약미체결)
  private String detail;           // detail (TEXT)
  private LocalDate dueDate;       // due_date (DATE)
  private LocalDateTime createdAt; // created_at (DATETIME)
}