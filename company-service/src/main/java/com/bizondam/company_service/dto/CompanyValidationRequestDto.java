package com.bizondam.company_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompanyValidationRequestDto {
    private String b_no;    // 사업자등록번호
    private String start_dt;    // 개업일자
    private String p_nm;    // 대표자 성명
    private String b_nm;    // 상호
}
