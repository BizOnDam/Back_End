package com.bizondam.matching_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierSearchConditionDto {
    private String detailCategoryCode;
    private int quantity;
    private LocalDate dueDate;
    private int maxLeadDays;
    private Long productId;
}
