package com.bizondam.contract_service.mapper;

import com.bizondam.contract_service.dto.ContractDto;
import com.bizondam.contract_service.dto.ContractItemDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractMapper {
    // 계약 생성
    void insertContract(ContractDto dto);

    // 계약 조회
    ContractDto findContractPdfData(@Param("requestId") Long requestId, @Param("responseId") Long responseId);

    // 계약 물품 조회
    List<ContractItemDto> findContractPdfItems(@Param("requestId") Long requestId, @Param("responseId") Long responseId);

    // 계약서 url 패치
    void updateContractFileUrl(@Param("contractId") Long contractId, @Param("url") String url);

    // 계약서 url 조회
    String findContractFileUrlByContractId(@Param("contractId") Long contractId);
}
