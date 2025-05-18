package com.bizondam.publicdata_service.dto;

import lombok.Builder;
import lombok.Getter;
import com.bizondam.publicdata_service.mapper.ProcurementHistoryMapper.SupplierCount;

@Getter
@Builder
public class SupplierCountDto {
    private String supplierBizno;
    private String supplierName;
    private String regionName;
    private long   cnt;

    // Mapper 프로젝션 → DTO 변환
    public static SupplierCountDto from(SupplierCount sc) {
        return SupplierCountDto.builder()
                .supplierBizno(sc.getSupplierBizno())
                .supplierName(sc.getSupplierName())
                .regionName(sc.getRegionName())
                .cnt(sc.getCnt())
                .build();
    }
}
