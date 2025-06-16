package com.bizondam.estimateservice.mapper;

import com.bizondam.estimateservice.dto.ContractCreateDto;
import com.bizondam.estimateservice.dto.ContractDto;
import com.bizondam.estimateservice.dto.ContractItemDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ContractMapper {
  // 계약서 생성
  void insertContract(ContractCreateDto contract);

  // 계약서 단일 객체 조회
  ContractDto findContractByRequestIdAndResponseId(@Param("requestId") Long requestId, @Param("responseId") Long responseId);

  // 품목 리스트 조회
  List<ContractItemDto> findContractItemsByRequestIdAndResponseId(@Param("requestId") Long requestId, @Param("responseId") Long responseId);

  // requestId를 이용한 견적 조회
  ContractDto findContractByRequestIdOnly(@Param("requestId") Long requestId);

  // requestId를 이용한 견적 item조회
  List<ContractItemDto> findContractItemsByRequestIdOnly(@Param("requestId") Long requestId);

  // 수요 기업용 리스트
  List<ContractDto> selectContractsByBuyerCompany(@Param("companyId") Long companyId);

  // 공급 기업용 리스트
  List<ContractDto> selectContractsBySupplierCompany(@Param("companyId") Long companyId);
}