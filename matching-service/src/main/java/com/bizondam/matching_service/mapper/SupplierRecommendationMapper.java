package com.bizondam.matching_service.mapper;

import com.bizondam.matching_service.dto.EstimateItemDto;
import com.bizondam.matching_service.dto.SupplierDto;
import com.bizondam.matching_service.dto.SupplierMetricDto;
import com.bizondam.matching_service.dto.SupplierSearchConditionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SupplierRecommendationMapper {
    List<EstimateItemDto> findEstimateItemsByRequestId(@Param("requestId") Long requestId);
    List<SupplierDto> findSuppliersByConditions(@Param("condition") SupplierSearchConditionDto condition);
    List<SupplierMetricDto> findMetricsByBiznosAndRequest(
            @Param("biznos") List<String> biznos,
            @Param("requestId") Long requestId
    );
}
