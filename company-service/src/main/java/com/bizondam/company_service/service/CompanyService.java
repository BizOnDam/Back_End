package com.bizondam.company_service.service;

import com.bizondam.company_service.domain.Company;
import com.bizondam.company_service.dto.CompanyRequest;
import com.bizondam.company_service.dto.CompanyResponse;
import com.bizondam.company_service.dto.CompanyValidationRequest;
import com.bizondam.company_service.exception.BusinessException;
import com.bizondam.company_service.mapper.CompanyMapper;
import com.bizondam.company_service.client.NationalTaxClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyMapper companyMapper;
    private final NationalTaxClient nationalTaxClient;

    //신규 회사 등록
    public Long createCompany(CompanyRequest dto) {
        // 1) 국세청 검증
        CompanyValidationRequest vDto = CompanyValidationRequest.builder()
            .b_no(dto.getBusinessNumber())
            .start_dt(dto.getStartDate())
            .p_nm(dto.getCeoNameKr())
            .b_nm(dto.getCompanyNameKr())
            .build();
        if (!nationalTaxClient.verify(vDto)) {
            throw new BusinessException("사업자 정보가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED.value());
        }

        // 2) 이미 등록된 회사인지 체크
        Company existing = companyMapper.selectByBusinessNumber(dto.getBusinessNumber());
        if (existing != null) {
            return existing.getCompanyId();
        }

        // 3) 신규 회사 Company 엔티티 생성 및 저장
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
        companyMapper.insertCompany(company);

        return company.getCompanyId();
    }

    // 회사 단건 조회 (ID 기준)
    public CompanyResponse getCompanyById(Long companyId) {
        Company company = companyMapper.selectCompanyById(companyId);
        if (company == null) {
            throw new BusinessException("등록되지 않은 기업입니다.", HttpStatus.NOT_FOUND.value());
        }
        return new CompanyResponse(company);
    }

    //전체 조회
    public List<Company> getAllCompanies() {
        return companyMapper.selectAllCompanies();
    }

    //수정
    public void updateCompany(Company company) {
        companyMapper.updateCompany(company);
    }

    //삭제
    public void deleteCompany(Long companyId) {
        companyMapper.deleteCompany(companyId);
    }
}
