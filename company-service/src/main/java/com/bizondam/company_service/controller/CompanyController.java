package com.bizondam.company_service.controller;

import com.bizondam.common.response.BaseResponse;
import com.bizondam.company_service.dto.*;
import com.bizondam.company_service.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
//    private final NationalTaxClient nationalTaxClient;

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
    public ResponseEntity<BaseResponse<CompanyValidateResultResponse>> validateBusiness(
        @RequestBody CompanyValidationRequest dto) {
        try {
            System.out.println(">>>> 받은 DTO: " + dto);

            dto.validate();  // 👉 직접 유효성 체크 수행
            CompanyRequest companyRequest = mapToCompanyRequest(dto);
            CompanyValidateResultResponse response = companyService.validateBusiness(companyRequest);

            if (!response.isValidBusinessNumber()) {
                return ResponseEntity
                    .badRequest()
                    .body(BaseResponse.fail("유효하지 않은 사업자 등록번호입니다.", null));
            }
            return ResponseEntity.ok(
                BaseResponse.success("사업자 등록번호 검증에 성공했습니다.", response)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .badRequest()
                .body(BaseResponse.fail("유효성 검사 실패: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.error(500, "서버 오류가 발생했습니다."));
        }
    }

    private CompanyRequest mapToCompanyRequest(CompanyValidationRequest validationRequest) {
        if (validationRequest == null) {
            throw new IllegalArgumentException("요청 데이터가 null입니다.");
        }
        return CompanyRequest.builder()
            .businessNumber(validationRequest.getB_no())
            .startDate(validationRequest.getStart_dt())
            .ceoNameKr(validationRequest.getP_nm())
            .companyNameKr(validationRequest.getB_nm())
            .build();
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long companyId) {
        CompanyResponse response = companyService.getCompanyInfo(companyId);
        return ResponseEntity.ok(response);
    }
}