package com.bizondam.contract_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ContractDto {
    private Long requestId;
    private Long responseId;
    private Long contractId;

    // 회사 정보
    private String buyerCompanyName;
    private String supplierCompanyName;
    private String buyerCompanyAddress;
    private String buyerCompanyAddressDetail;

    // 사용자 정보
    private String buyerUserName;
    private String buyerUserPhone;
    private String supplierUserName;
    private String supplierUserPhone;

    private String dueDate; // 납품기한
    private Long totalPrice; // 총 금액
    private String paymentTerms; // 결제조건
    private String warranty; // 보증기간
    private LocalDateTime contractCreatedAt; // 계약 체결일

    private List<ContractItemDto> items; // 품목 리스트
}
