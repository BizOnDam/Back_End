package com.bizondam.estimateservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractCreateDto {
    private Long contractId;
    private Long responseId;
    private Long buyerCompanyId;
    private Long supplierCompanyId;
    private Long buyerUserId;
    private Long supplierUserId;
    private Integer status;
    private String contractFileUrl;
}