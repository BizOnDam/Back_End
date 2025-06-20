package com.bizondam.matching_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierMetricDto {
    private String detailCategoryName; // pmb.detail_category_name
    private String supplierBizno;    // ph.supplier_bizno
    private String supplierName;     // ph.supplier_name
    private Integer matchedQuantity; // MAX(ph.quantity)
    private Integer leadTimeDays;    // MIN(DATEDIFF(pc.delivery_deadline, pc.first_contract_date))
    private Integer transactionCount;// COUNT(*)
}