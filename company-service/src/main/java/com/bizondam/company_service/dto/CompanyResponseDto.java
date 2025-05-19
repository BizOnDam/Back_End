package com.bizondam.company_service.dto;

import com.bizondam.company_service.domain.Company;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CompanyResponseDto {   // 기업 정보 조회 및 등록 후 응답할 때 사용

    private Long companyId;
    private String companyNameKr;
    private String companyNameEn;
    private String ceoNameKr;
    private String ceoNameEn;
    private String startDate;
    private String businessNumber;
    private String phoneNumber;
    private String faxNumber;
    private String postcode;
    private String address;
    private String addressDetail;
    private String businessType;
    private LocalDateTime createdAt;

    public CompanyResponseDto(Company company) {
        this.companyId = company.getCompanyId();
        this.companyNameKr = company.getCompanyNameKr();
        this.companyNameEn = company.getCompanyNameEn();
        this.ceoNameKr = company.getCeoNameKr();
        this.ceoNameEn = company.getCeoNameEn();
        this.startDate = company.getStartDate();
        this.businessNumber = company.getBusinessNumber();
        this.phoneNumber = company.getPhoneNumber();
        this.faxNumber = company.getFaxNumber();
        this.postcode = company.getPostcode();
        this.address = company.getAddress();
        this.addressDetail = company.getAddressDetail();
        this.businessType = company.getBusinessType();
        this.createdAt = company.getCreatedAt();
    }
}
