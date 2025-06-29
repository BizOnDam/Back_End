package com.bizondam.contract_service.service;

import com.bizondam.common.exception.CustomException;
import com.bizondam.contract_service.dto.ContractDto;
import com.bizondam.contract_service.dto.ContractHistoryDto;
import com.bizondam.contract_service.dto.ContractItemDto;
import com.bizondam.contract_service.dto.ContractListResponseDto;
import com.bizondam.contract_service.dto.CounterpartyInfoDto;
import com.bizondam.contract_service.dto.TradeSummaryDto;
import com.bizondam.contract_service.exception.ContractErrorCode;
import com.bizondam.contract_service.mapper.ContractMapper;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractMapper contractMapper;
    private final ContractPdfService contractPdfService;

    @Transactional
    public String createContractAndGeneratePdf(Long requestId, Long responseId) throws IOException {
        log.info("계약 생성 시작 - requestId={}, responseId={}", requestId, responseId);

        // 1. 계약서 메타 정보 조회 (contract_id 없는 상태)
        ContractDto dto = contractMapper.findContractPdfData(requestId, responseId);
        if (dto == null) {
            throw new IllegalArgumentException("계약 데이터를 찾을 수 없습니다.");
        }

        contractMapper.updateRequestStatus(requestId, 3);
        contractMapper.updateResponseStatus(requestId, 3);
        dto.setStatus(1); // 거래중 상태

        // 2. contracts 테이블에 INSERT (contract_id 생성됨)
        contractMapper.insertContract(dto);
        log.info("계약 저장 완료 - contractId={}", dto.getContractId());

        // 3. PDF 생성 + GCS 업로드 + URL 저장
        return contractPdfService.generateAndUploadContractPdf(requestId, responseId);
    }

    // 계약서 url 조회 서비스
    public String getContractFileUrlByContractId(Long contractId) {
        String url = contractMapper.findContractFileUrlByContractId(contractId);
        if (url == null) {
            throw new IllegalArgumentException("계약서 URL을 찾을 수 없습니다.");
        }
        return url;
    }

    // 전체 거래 현황 조회
    public TradeSummaryDto getTradeSummary(Long companyId) {
        int inProgress = contractMapper.countContractsByStatus(companyId, 1);
        int completed = contractMapper.countContractsByStatus(companyId, 2);
        int pendingEstimates = contractMapper.countPendingEstimates(companyId);

        TradeSummaryDto dto = new TradeSummaryDto();
        dto.setInProgressContracts(inProgress);
        dto.setCompletedContracts(completed);
        dto.setTotalContracts(inProgress + completed);
        dto.setPendingEstimates(pendingEstimates);
        return dto;
    }

    // 계약 완료
    public void completeContract(Long contractId) {
        int updatedRows = contractMapper.updateContractStatusToCompleted(contractId);
        if (updatedRows == 0) {
            // 업데이트 안되면 에러 발생: 이미 완료된 계약
            throw new CustomException(ContractErrorCode.CONTRACT_ALREADY_COMPLETED);
        }
    }

    // 계약 리스트
    public List<ContractListResponseDto> getContractList(
        Long userId, Long companyId, String role, String userRole, LocalDate date) {

        List<ContractListResponseDto> contracts;

        // CEO: 회사의 모든 계약
        if ("CEO".equalsIgnoreCase(userRole)) {
            if ("BUYER".equalsIgnoreCase(role)) {
                contracts = contractMapper.findContractsByBuyer(companyId, null); // userId null
            } else {
                contracts = contractMapper.findContractsBySupplier(companyId, null); // userId null
            }
        }
        // STAFF: 본인이 요청/응답한 계약만
        else {
            if ("BUYER".equalsIgnoreCase(role)) {
                contracts = contractMapper.findContractsByBuyer(companyId, userId); // userId 필터
            } else {
                contracts = contractMapper.findContractsBySupplier(companyId, userId); // userId 필터
            }
        }

        // 이하 품목 및 날짜 필터링 로직 동일
        for (ContractListResponseDto dto : contracts) {
            List<ContractItemDto> items = contractMapper.findContractItemsByRequestIdAndResponseId(
                dto.getRequestId(), dto.getResponseId());
            List<String> itemNames = items.stream()
                .map(ContractItemDto::getDetailCategoryName)
                .collect(Collectors.toList());
            dto.setItemNames(itemNames);
        }

        List<ContractListResponseDto> filtered = contracts.stream()
            .filter(dto -> {
                if (date == null) return true;
                return date.equals(dto.getContractDate()) || date.equals(dto.getDueDate());
            })
            .sorted(Comparator.comparing(ContractListResponseDto::getContractDate).reversed())
            .collect(Collectors.toList());

        return filtered;
    }

    // 거래 이력
    public List<ContractHistoryDto> getContractHistoryByCompanyId(Long companyId) {
        return contractMapper.selectContractHistoryByCompanyId(companyId);
    }

    // 계약자 정보 조회
    public CounterpartyInfoDto getCounterpartyInfo(Long contractId, Long userId) {
        ContractDto contractDto = contractMapper.findContractDtoById(contractId);
        if (contractDto == null) {
            throw new CustomException(ContractErrorCode.CONTRACT_NOT_FOUND);
        }

        // 1. userId로 company_id 조회
        Long userCompanyId = contractMapper.findCompanyIdByUserId(userId);
        if (userCompanyId == null) {
            throw new CustomException(ContractErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 2. company_id 비교 후, 상대방 정보 조회
        if (userCompanyId.equals(contractDto.getBuyerCompanyId())) {
            // 내가 buyer면 supplier 정보 조회
            return contractMapper.getSupplierInfoByBuyer(contractDto.getBuyerCompanyId(), contractId);
        } else if (userCompanyId.equals(contractDto.getSupplierCompanyId())) {
            // 내가 supplier면 buyer 정보 조회
            return contractMapper.getBuyerInfoBySupplier(contractDto.getSupplierCompanyId(), contractId);
        } else {
            throw new CustomException(ContractErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
}