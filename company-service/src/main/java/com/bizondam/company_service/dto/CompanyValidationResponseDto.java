package com.bizondam.company_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanyValidationResponseDto {
    private String status_code;
    private int request_cnt;
    private int valid_cnt;
    private List<CompanyValidationData> data;

    @Getter
    @Setter
    public static class CompanyValidationData {
        private String b_no;
        private String valid;       // "01" = 일치, 02 = 불일치
        private String valid_msg;   // 불일치인 경우 "확인할 수 없습니다"

        private RequestParam request_param;
        private Status status;

        @Getter
        @Setter
        public static class RequestParam {
            private String b_no;
            private String start_dt;
            private String p_nm;
            private String p_nm2;
            private String b_nm;
            private String corp_no;
            private String b_sector;
            private String b_type;
            private String b_adr;
        }

        @Getter
        @Setter
        public static class Status {
            private String b_no;
            private String b_stt;   // 상태 (ex. 계속사업자)
            private String b_stt_cd;    // ex. 01
            private String tax_type;
            private String tax_type_cd;
            private String end_dt;
            private String utcc_yn;
            private String tax_type_change_dt;
            private String invoice_apply_dt;
            private String rbf_tax_type;
            private String rbf_tax_type_cd;
        }
    }
}
