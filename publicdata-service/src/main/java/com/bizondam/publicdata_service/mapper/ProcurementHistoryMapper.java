package com.bizondam.publicdata_service.mapper;

import com.bizondam.publicdata_service.domain.ProcurementHistory;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProcurementHistoryMapper {

    // XML <insert id="insertHistory">에 매핑
//    void insertHistory(ProcurementHistory history);

    // XML <select id="countBySupplierForProduct">에 매핑
    List<SupplierCount> countBySupplierForProduct(String productId);

    List<ProcurementHistory> selectAll();

    // XML <resultMap id="SupplierCountMap">에 매핑
    @Data
    public static class SupplierCount {
        private String supplierBizno;
        private String supplierName;
        private String regionName;
        private long   cnt;
    }
}
