package com.bizondam.company_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.bizondam.company_service.client.NationalTaxClient;
import com.bizondam.company_service.dto.CompanyRequestDto;
import com.bizondam.company_service.dto.CompanyResponseDto;
import com.bizondam.company_service.dto.CompanyValidationRequestDto;
import com.bizondam.company_service.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "기업 등록 API", description = "기업 정보 등록 및 조회 관련 API")
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);  // Logger 객체 생성

    private final CompanyService companyService;
    private final NationalTaxClient nationalTaxClient;

    @PostMapping
    //TODO 반환 된다면 다 타입 dto로 바꾸기
    public ResponseEntity<String> registerCompany(@RequestBody CompanyRequestDto dto) {
        companyService.registerCompany(dto);
        return ResponseEntity.ok("등록 완료");
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkDuplicate(@RequestParam String businessNumber) {
        boolean isDuplicate = companyService.isDuplicateBusinessNumber(businessNumber);
        // TODO 예외처리
        if (isDuplicate) {
            logger.info("이미 등록된 사업자 번호입니다.");  // 등록된 경우 콘솔에 출력
        } else {
            logger.info("등록 가능한 사업자 번호입니다.");  // 등록되지 않은 경우 콘솔에 출력
        }

        return ResponseEntity.ok(isDuplicate);
//        return ResponseEntity.ok(companyService.isDuplicateBusinessNumber(businessNumber));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompany(id));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateBusiness(@RequestBody CompanyValidationRequestDto dto) {
        boolean result = nationalTaxClient.verify(dto);
        return ResponseEntity.ok(result);
    }
}
