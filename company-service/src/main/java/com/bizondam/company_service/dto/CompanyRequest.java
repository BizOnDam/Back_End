package com.bizondam.company_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequest {  // 프론트에서 기업 등록 요정을 보낼 때 사용
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
}
