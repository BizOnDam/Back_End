package com.bizondam.estimateservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDetailDto {
    private Long productId;
    private String detailCategoryName;
}
