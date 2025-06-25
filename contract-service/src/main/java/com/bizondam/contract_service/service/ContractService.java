package com.bizondam.contract_service.service;

import com.bizondam.contract_service.dto.ContractDto;
import com.bizondam.contract_service.mapper.ContractMapper;
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
}
