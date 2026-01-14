package com.bizondam.estimateservice.mapper;

import com.bizondam.estimateservice.dto.ContractDto;
import com.bizondam.estimateservice.dto.ContractItemDto;
import com.bizondam.estimateservice.dto.EstimateRequestCreateDto;
import com.bizondam.estimateservice.model.EstimateRequestItem;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EstimateRequestMapper {
  // request 테이블에 헤더(메타) 정보 INSERT
  void insertEstimateRequest(EstimateRequestCreateDto dto);

  // 공급 업체 지정 시, estimate_request.supplier_company_id UPDATE
  void updateSupplierCompany(@Param("requestId") Long requestId, @Param("businessNumber") String businessNumber);

  // 계약 미체결 시, estimate_request.status를 UPDATE
  void updateRequestStatus(@Param("requestId") Long requestId, @Param("status") Integer status);

  // itemId 반환
  void insertEstimateRequestItemWithReturnId(EstimateRequestItem item);

  // request, response 정보, 품목 리스트 조회
  ContractDto findContractByRequestIdAndResponseId(@Param("requestId") Long requestId, @Param("responseId") Long responseId);
  List<ContractItemDto> findContractItemsByRequestIdAndResponseId(@Param("requestId") Long requestId, @Param("responseId") Long responseId);

  // requestId를 이용한 견적, item 조회
  ContractDto findRequestByRequestIdOnly(@Param("requestId") Long requestId);
  List<ContractItemDto> findRequestItemsByRequestIdOnly(@Param("requestId") Long requestId);

  // 견적 요청서 조회
  List<ContractDto> selectRequestsByBuyerCompanyAndUser(@Param("companyId") Long companyId, @Param("userId") Long userId);
  List<ContractDto> selectRequestsBySupplierCompanyAndUser(@Param("companyId") Long companyId, @Param("userId") Long userId);

  // 수요 기업용, 공급 기업용 리스트
  List<ContractDto> selectRequestsByBuyerCompany(@Param("companyId") Long companyId);
  List<ContractDto> selectRequestsBySupplierCompany(@Param("companyId") Long companyId);
}