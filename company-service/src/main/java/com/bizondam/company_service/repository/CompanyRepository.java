package com.bizondam.company_service.repository;

import com.bizondam.company_service.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    // 사업자 등록번호로 기업 조회
    Optional<Company> findByBusinessNumber(String businessNumber);
    // 특정 사업자번호 존재 여부 확인 (중복확인)
    boolean existsByBusinessNumber(String businessNumber);
}