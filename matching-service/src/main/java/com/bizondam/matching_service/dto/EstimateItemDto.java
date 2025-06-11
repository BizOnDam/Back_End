package com.bizondam.matching_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EstimateItemDto {  // 견적 요청에 포함된 품목 정보를 담는 DTO
    private Long productId;
    private String detailCategoryCode;
    private String specification;
    private int quantity;
    private LocalDate requestDate;
    private LocalDate dueDate;
}