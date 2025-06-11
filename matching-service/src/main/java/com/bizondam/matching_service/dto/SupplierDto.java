package com.bizondam.matching_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SupplierDto {
    @Schema(description = "세부품명")
    private String detailCategoryName;

    @Schema(description = "공급자 사업자번호")
    private String supplierBizno;

    @Schema(description = "공급자 이름")
    private String supplierName;

    @Schema(description = "매칭된 최대 수량")
    private Integer matchedQuantity;

    @Schema(description = "거래 체결 횟수")
    private Integer transactionCount;

    @Schema(description = "리드타임 (delivery_deadline과 first_contract_date 차이, 일수)")
    private Integer leadTimeDays;
}
