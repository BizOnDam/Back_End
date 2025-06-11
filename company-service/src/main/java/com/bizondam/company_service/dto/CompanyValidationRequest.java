package com.bizondam.company_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized           // @Builder와 함께 Jackson이 빌더를 사용하도록 해줌
@NoArgsConstructor     // Jackson이 인스턴스 생성 시 기본 생성자를 사용하게 함
@AllArgsConstructor    // 프로퍼티 기반 생성자도 제공
public class CompanyValidationRequest {
    @NotBlank
    @JsonProperty("b_no")
    private String b_no; // 사업자등록번호

    @NotBlank
    @JsonProperty("start_dt")
    private String start_dt; // 개업일자

    @NotBlank
    @JsonProperty("p_nm")
    private String p_nm; // 대표자 성명

    @NotBlank
    @JsonProperty("b_nm")
    private String b_nm; // 상호
}
