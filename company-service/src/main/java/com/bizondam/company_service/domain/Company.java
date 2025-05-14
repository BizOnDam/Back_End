package com.bizondam.company_service.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Persistent;

import java.time.LocalDateTime;

@Entity // 테이블과 매핑되는 클래스
@Table(name = "companies")  // DB의 companies 테이블과 매핑
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id // 기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL에서 자동 증가 방식 사용
    private Long companyId;

    // 회사 정보
    @Column(nullable = false, length = 100) // 한글 회사명 필수
    private String companyNameKr;

    @Column(length = 100)
    private String companyNameEn;

    // 대표자 정보
    @Column(nullable = false, length = 50)  // 대표자 한글 이름 필수
    private String ceoNameKr;

    @Column(length = 50)
    private String ceoNameEn;

    @Column(length = 10)
    private String startDate; // 형식: yyyyMMdd

    // 사업자 등록번호 필수, 중복 x
    @Column(nullable = false, length = 20, unique = true)
    private String businessNumber;

    // 연락처
    @Column(nullable = false, length = 15)  // 전화번호 필수
    private String phoneNumber;

    @Column(length = 20)
    private String faxNumber;

    // 주소
    @Column(length = 10)
    private String postcode;

    @Column(length = 255)
    private String address;

    @Column(length = 255)
    private String addressDetail;

    // 업종
    @Column(length = 100)
    private String businessType;

    // 둥록 일시
    @Persistent
    private LocalDateTime createdAt;
}