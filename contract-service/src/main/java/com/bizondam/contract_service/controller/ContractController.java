package com.bizondam.contract_service.controller;

import com.bizondam.common.response.BaseResponse;
import com.bizondam.contract_service.service.ContractPdfService;
import com.bizondam.contract_service.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractPdfService contractPdfService;
    private final ContractService contractService;

    // 견적 응답 기반 계약 생성 + PDF 생성 + URL 저장
    @PostMapping("/{requestId}/{responseId}/generate")
    public ResponseEntity<BaseResponse<String>> generateContractPdf( @PathVariable Long requestId, @PathVariable Long responseId) {
        log.info("[API 호출] 계약 생성 요청 - requestId={}, responseId={}", requestId, responseId);
        try {
            String fileUrl = contractService.createContractAndGeneratePdf(requestId, responseId);
            log.info("[성공] 계약 생성 및 PDF 저장 완료 - fileUrl={}", fileUrl);
            return ResponseEntity.ok(BaseResponse.success("계약 생성 및 PDF 완료", fileUrl));
        } catch (IllegalArgumentException e) {
            log.warn("[데이터 오류] {}", e.getMessage());
            return ResponseEntity.badRequest().body(BaseResponse.error(400, e.getMessage()));
        } catch (IOException e) {
            log.error("[PDF 생성 오류] requestId={}, responseId={}, error={}", requestId, responseId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(BaseResponse.error(500, "PDF 생성 중 오류가 발생했습니다."));
        } catch (Exception e) {
            log.error("[예상치 못한 오류] requestId={}, responseId={}, error={}", requestId, responseId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(BaseResponse.error(500, "계약 생성 중 문제가 발생했습니다."));
        }
    }

    // 계약서 url 조회 컨트롤러
    @GetMapping("/{contractId}/file-url")
    public ResponseEntity<BaseResponse<String>> getContractFileUrl(@PathVariable Long contractId) {
        String url = contractService.getContractFileUrlByContractId(contractId);
        return ResponseEntity.ok(BaseResponse.success(url));
    }
}
