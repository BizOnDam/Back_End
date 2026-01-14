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
    @JsonProperty("b_no")
    private String b_no; // 사업자등록번호

    @JsonProperty("start_dt")
    private String start_dt; // 개업일자

    @JsonProperty("p_nm")
    private String p_nm; // 대표자 성명

    @JsonProperty("b_nm")
    private String b_nm; // 상호

    public void validate() {
        if (isBlank(b_no)) throw new IllegalArgumentException("사업자등록번호는 필수입니다.");
        if (isBlank(start_dt)) throw new IllegalArgumentException("개업일자는 필수입니다.");
        if (isBlank(p_nm)) throw new IllegalArgumentException("대표자 성명은 필수입니다.");
        if (isBlank(b_nm)) throw new IllegalArgumentException("상호는 필수입니다.");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("b_no: %s, start_dt: %s, p_nm: %s, b_nm: %s",
            b_no, start_dt, p_nm, b_nm);
    }
}
