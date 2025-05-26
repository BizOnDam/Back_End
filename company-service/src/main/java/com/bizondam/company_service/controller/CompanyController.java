package com.bizondam.company_service.controller;

import com.bizondam.company_service.client.NationalTaxClient;
import com.bizondam.company_service.dto.*;
import com.bizondam.company_service.service.CompanyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "기업 등록 API", description = "기업 정보 등록 및 조회 관련 API")
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final NationalTaxClient nationalTaxClient;

    @PostMapping("/register")
    public ResponseEntity<Long> registerCompany(@RequestBody CompanyRequest dto) {
        Long companyId = companyService.createCompany(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyId);
    }

    // 회사 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        CompanyResponse dto = companyService.getCompanyById(id);
        return ResponseEntity.ok(dto);
    }

    // 국세청 사업자 등록 번호 검증
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateBusiness(@RequestBody CompanyValidationRequest dto) {
        boolean result = nationalTaxClient.verify(dto);
        return ResponseEntity.ok(result);
    }
}

