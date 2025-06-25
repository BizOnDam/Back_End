package com.bizondam.contract_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractItemDto {
    private Long itemId;
    private String specification;   // 규격(개, 다스)
    private Integer quantity;       // 수량

    private String categoryName;        // 품명
    private String detailCategoryName;  // 세부품명

    private Integer unitPrice;          // 단가
    private Integer deliveryDays;       // 납품 소요일
}
