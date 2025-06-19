package com.bizondam.estimateservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractDto {
  // estimate_request
  private Long requestId;
  private Long buyerUserId;
  private Long buyerCompanyId;
  private Long supplierCompanyId;
  private Integer requestStatus;
  private String detail;
  private String dueDate;
  private LocalDateTime requestCreatedAt;

  // estimate_response
  private Long responseId;
  private Long supplierUserId;
  private Integer responseStatus;
  private Long totalPrice;
  private String paymentTerms;
  private String warranty;
  private String specialTerms;
  private LocalDateTime responseCreatedAt;

  // 회사 이름
  private String buyerCompanyName;
  private String supplierCompanyName;

  // 회사 주소
  private String buyerCompanyAddress;
  private String buyerCompanyAddressDetail;

  // 수요 업체 담당자 정보
  private String buyerUserName;
  private String buyerUserPhone;

  // 공급 업체 담당자 정보
  private String supplierUserName;
  private String supplierUserPhone;

  // 계약 정보
  private Long contractId;
  private LocalDateTime contractCreatedAt;

  // 하위 항목들
  private List<ContractItemDto> items;
}