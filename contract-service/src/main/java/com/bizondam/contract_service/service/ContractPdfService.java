package com.bizondam.contract_service.service;

import com.bizondam.contract_service.dto.ContractDto;
import com.bizondam.contract_service.dto.ContractItemDto;
import com.bizondam.contract_service.mapper.ContractMapper;
import com.bizondam.contract_service.util.ContractPdfGenerator;
import com.bizondam.contract_service.util.GcsUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractPdfService {

    private final ContractMapper contractMapper;
    private final ContractPdfGenerator contractPdfGenerator;
    private final GcsUploader gcsUploader;

    @Transactional
    public String generateAndUploadContractPdf(Long requestId, Long responseId) throws IOException {
        log.info("계약 PDF 생성 시작 - requestId={}, responseId={}", requestId, responseId);

        // 1. 데이터 조회
        ContractDto dto = contractMapper.findContractPdfData(requestId, responseId);
        List<ContractItemDto> items = contractMapper.findContractPdfItems(requestId, responseId);
        dto.setItems(items);
        log.info("계약 데이터 조회 완료 - contractId={}", dto.getContractId());

        // 2. PDF 생성
        byte[] pdfBytes = contractPdfGenerator.generatePdf(dto);
        log.info("PDF 생성 완료 - 바이트 크기={} bytes", pdfBytes.length);

        // 3. GCS에 업로드 (파일명: contract-{contractId}-타임스탬프.pdf)
        String fileName = String.format("contracts/contract-%d-%s.pdf", dto.getContractId(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        String url = gcsUploader.upload(fileName, pdfBytes, "application/pdf");
        log.info("GCS 업로드 완료 - fileName={}, url={}", fileName, url);

        // 4. DB에 URL 저장
        contractMapper.updateContractFileUrl(dto.getContractId(), url);
        log.info("계약 DB URL 업데이트 완료 - contractId={}, url={}", dto.getContractId(), url);

        return url;
    }
}
