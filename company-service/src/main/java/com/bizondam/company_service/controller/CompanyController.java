package com.bizondam.company_service.controller;

import com.bizondam.common.response.BaseResponse;
import com.bizondam.company_service.client.NationalTaxClient;
import com.bizondam.company_service.dto.*;
import com.bizondam.company_service.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "기업 등록 API", description = "기업 정보 등록 및 조회 관련 API")
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final NationalTaxClient nationalTaxClient;

    @Operation(summary = "기업 등록 API", description = "기업의 최초 가입자 회원가입 시 기업을 등록해주는 API")
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<CompanyResponse>> registerCompany(
        @Valid @RequestBody CompanyRequest dto) {
        CompanyResponse response = companyService.createCompany(dto);
        return ResponseEntity
            .ok(BaseResponse.success("기업 등록에 성공했습니다.", response));
    }

    @Operation(summary = "사업자 등록 번호 검증 API", description = "사업자 등록 번호 검증 및 기업 가입 여부 확인 API")
    @PostMapping("/validate")
    public ResponseEntity<CompanyValidateResultResponse> validateBusiness(
        @Valid @RequestBody CompanyValidationRequest dto) {
        CompanyValidateResultResponse response = nationalTaxClient.validateBusiness(dto);
        return ResponseEntity.ok(response);
    }
}