package com.bizondam.contract_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractItemDto {
    private Long itemId;
    private String specification;   // 규격
    private Integer quantity;       // 수량

    private String categoryName;        // 품명
    private String detailCategoryName;  // 단위 대체 가능

    private Integer unitPrice;          // 가격
    private Integer deliveryDays;       // 리드타임
}
