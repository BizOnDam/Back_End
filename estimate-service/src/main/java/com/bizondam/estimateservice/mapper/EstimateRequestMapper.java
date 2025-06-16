package com.bizondam.estimateservice.mapper;

import com.bizondam.estimateservice.dto.EstimateRequestCreateDto;
import com.bizondam.estimateservice.dto.EstimateRequestItemDto;
import com.bizondam.estimateservice.model.EstimateRequestItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EstimateRequestMapper {
  // 1) request 테이블에 헤더(메타) 정보 INSERT 후, 자동 생성된 request_id를 dto나 파라미터 객체에 세팅해야 함
  void insertEstimateRequest(EstimateRequestCreateDto dto);
  // 2) estimate_request_item 테이블에 각 품목 INSERT
  void insertEstimateRequestItem(@Param("requestId") Long requestId,
      @Param("item") EstimateRequestItemDto item);
  // 3) 공급 업체 지정 시, estimate_request.supplier_company_id UPDATE
  void updateSupplierCompany(@Param("requestId") Long requestId,
      @Param("businessNumber") String businessNumber);
  // 4) 계약 체결/미체결 시, estimate_request.status를 UPDATE
  void updateRequestStatus(@Param("requestId") Long requestId,
      @Param("status") Integer status);
  // 5) itemId 반환
  void insertEstimateRequestItemWithReturnId(EstimateRequestItem item);
}