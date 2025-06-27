package com.bizondam.contract_service.mapper;

import com.bizondam.contract_service.dto.ContractDto;
import com.bizondam.contract_service.dto.ContractHistoryDto;
import com.bizondam.contract_service.dto.ContractItemDto;
import com.bizondam.contract_service.dto.ContractListResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractMapper {
    // 계약 생성
    void insertContract(ContractDto dto);

    // 계약 조회, 물품 조회
    ContractDto findContractPdfData(@Param("requestId") Long requestId, @Param("responseId") Long responseId);
    List<ContractItemDto> findContractPdfItems(@Param("requestId") Long requestId, @Param("responseId") Long responseId);

    // 계약서 url 패치, 조회
    void updateContractFileUrl(@Param("contractId") Long contractId, @Param("url") String url);
    String findContractFileUrlByContractId(@Param("contractId") Long contractId);

    // 전체 거래 건수 조회
    int countContractsByStatus(@Param("companyId") Long companyId, @Param("status") int status);
    int countPendingEstimates(@Param("companyId") Long companyId);

    // 거래 완료로 업데이트
    int updateContractStatusToCompleted(@Param("contractId") Long contractId);

    // 계약서 단일 객체 조회
    ContractDto findContractByRequestIdAndResponseId(@Param("requestId") Long requestId, @Param("responseId") Long responseId);

    // 품목 리스트 조회
    List<ContractItemDto> findContractItemsByRequestIdAndResponseId(@Param("requestId") Long requestId, @Param("responseId") Long responseId);

    // 계약 리스트 - 수요 기업용, 공급 기업용
    List<ContractListResponseDto> findContractsByBuyer(@Param("companyId") Long companyId, @Param("userId") Long userId);
    List<ContractListResponseDto> findContractsBySupplier(@Param("companyId") Long companyId, @Param("userId") Long userId);

    // 거래 이력 조회
    List<ContractHistoryDto> selectContractHistoryByCompanyId(@Param("companyId") Long companyId);

    // 계약 체결 시 request, response status 변경
    void updateRequestStatus(@Param("requestId") Long requestId, @Param("status") Integer status);
    void updateResponseStatus(@Param("responseId") Long responseId, @Param("status") Integer status);
}