package com.bizondam.company_service.service;

import com.bizondam.company_service.domain.Company;
import com.bizondam.company_service.dto.CompanyRequestDto;
import com.bizondam.company_service.dto.CompanyResponseDto;
import com.bizondam.company_service.dto.CompanyValidationRequestDto;
import com.bizondam.company_service.repository.CompanyRepository;
import com.bizondam.company_service.client.NationalTaxClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;  // 기업 정보 조회 및 저장
    private final NationalTaxClient nationalTaxClient;  // 국세청 API 사용

    // 사업자번호 중복 여부 확인
    public boolean isDuplicateBusinessNumber(String businessNumber) {
        return companyRepository.existsByBusinessNumber(businessNumber);
    }

    // 기업 등록
    public void registerCompany(CompanyRequestDto dto) {
        if (isDuplicateBusinessNumber(dto.getBusinessNumber())) {   // 중복 체크
            throw new IllegalArgumentException("이미 등록된 사업자 번호입니다.");
        }

        // 국세청 검증 DTO 생성
        CompanyValidationRequestDto validationDto = CompanyValidationRequestDto.builder()
                .b_no(dto.getBusinessNumber())
                .start_dt(dto.getStartDate())
                .p_nm(dto.getCeoNameKr())
                .b_nm(dto.getCompanyNameKr())
                .build();

        // 진위 확인 (국세청 API 호출)
        boolean isValid = nationalTaxClient.verify(validationDto);
        if (!isValid) {
            throw new IllegalArgumentException("사업자 정보가 일치하지 않습니다.");
        }

        // 엔티티 생성 및 저장
        Company company = Company.builder()
                .companyNameKr(dto.getCompanyNameKr())
                .companyNameEn(dto.getCompanyNameEn())
                .ceoNameKr(dto.getCeoNameKr())
                .ceoNameEn(dto.getCeoNameEn())
                .startDate(dto.getStartDate())
                .businessNumber(dto.getBusinessNumber())
                .phoneNumber(dto.getPhoneNumber())
                .faxNumber(dto.getFaxNumber())
                .postcode(dto.getPostcode())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .businessType(dto.getBusinessType())
                .createdAt(LocalDateTime.now())
                .build();
        companyRepository.save(company);
    }

    // 기업 단건 조회
    public CompanyResponseDto getCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 기업입니다."));
        return new CompanyResponseDto(company);
    }
}
