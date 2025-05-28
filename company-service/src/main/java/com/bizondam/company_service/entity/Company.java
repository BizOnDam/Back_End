package com.bizondam.company_service.entity;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    private Long companyId; //기본키, 자동 증가
    //회사 정보
    private String companyNameKr;   //한글 회사명 필수
    private String companyNameEn;
    //대표자 정보
    private String ceoNameKr;   // 대표자 한글 이름 필수
    private String ceoNameEn;
    private String startDate;   // yyyyMMdd
    private String businessNumber;  // 사업자 등록번호 필수, 중복 x
    // 연락처
    private String phoneNumber; // 휴대폰 번호 필수
    private String faxNumber;
    // 주소
    private String postcode;
    private String address;
    private String addressDetail;
    private String businessType;    // 업종
    private LocalDateTime createdAt;    // 둥록 일시
}
