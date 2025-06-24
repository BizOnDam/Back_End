package com.bizondam.contract_service.controller;

import com.bizondam.contract_service.service.ContractPdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractPdfService contractPdfService;

    // 계약 체결 시 pdf 생성 컨트롤러 호출
    @PostMapping("/{requestId}/{responseId}/generate-pdf")
    public ResponseEntity<String> generateContractPdf(
            @PathVariable Long requestId,
            @PathVariable Long responseId
    ) {
        log.info("계약서 PDF 생성 요청: requestId={}, responseId={}", requestId, responseId);
        try {
            String url = contractPdfService.generateAndUploadContractPdf(requestId, responseId);
            log.info("PDF 생성 및 업로드 성공: {}", url);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            log.error("PDF 생성 실패: requestId={}, responseId={}, error={}", requestId, responseId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("PDF 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
