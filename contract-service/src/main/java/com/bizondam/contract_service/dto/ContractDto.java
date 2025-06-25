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
    private Long buyerCompanyId;
    private String buyerCompanyName;
    private String buyerCompanyAddress;
    private String buyerCompanyAddressDetail;

    private Long supplierCompanyId;
    private String supplierCompanyName;


    // 사용자 정보
    private Long buyerUserId;
    private String buyerUserName;
    private String buyerUserPhone;

    private Long supplierUserId;
    private String supplierUserName;
    private String supplierUserPhone;

    // 견적/계약 관련 정보
    private String detail; // 견적 요청 상세 설명
    private String dueDate; // 납품 기한

    private Long totalPrice; // 총 금액
    private String paymentTerms; // 결제 조건
    private String warranty; // 보증 기간
    private String specialTerms; // 특별 조항

    // 계약서 정보
    private Integer status; // 계약 상태
    private String contractFileUrl; // 계약서 파일 URL
    private LocalDateTime contractCreatedAt; // 계약 생성일
    private LocalDateTime contractUpdatedAt; // 계약 수정일

    private List<ContractItemDto> items; // 품목 리스트
}
