package com.bizondam.company_service.service;

import com.bizondam.company_service.dto.CompanyResponse;
import com.bizondam.company_service.dto.CompanyValidateResultResponse;
import com.bizondam.company_service.entity.Company;
import com.bizondam.company_service.dto.CompanyRequest;
import com.bizondam.company_service.dto.CompanyValidationRequest;
import com.bizondam.company_service.mapper.CompanyMapper;
import com.bizondam.company_service.client.NationalTaxClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {
  private final CompanyMapper companyMapper;
  private final NationalTaxClient nationalTaxClient;

  // 국세청 검증 + DB 존재 여부 확인 메서드
  public CompanyValidateResultResponse validateBusiness(CompanyRequest dto) {
    CompanyValidateResultResponse response = new CompanyValidateResultResponse();

    // 1) 국세청 검증
    CompanyValidationRequest vDto = CompanyValidationRequest.builder()
        .b_no(dto.getBusinessNumber())
        .start_dt(dto.getStartDate())
        .p_nm(dto.getCeoNameKr())
        .b_nm(dto.getCompanyNameKr())
        .build();

    boolean validBusinessNumber = nationalTaxClient.verify(vDto);

    // 2) 이미 등록된 회사인지 체크
    Company existing = companyMapper.selectByBusinessNumber(dto.getBusinessNumber());
    boolean alreadyRegistered = (existing != null);
    System.out.println("existing: " + existing);
    if (existing != null) {
      System.out.println("companyId: " + existing.getCompanyId());
    }

    // 3) 메시지 세팅
    String msg;
    if (!validBusinessNumber) {
      msg = "사업자 정보가 일치하지 않습니다.";
      response.setCompanyId(null);
    } else if (alreadyRegistered) {
      msg = "이미 등록된 기업입니다.";
      response.setCompanyId(existing.getCompanyId());
    } else {
      msg = "사업자 등록번호가 유효하며, 최초 가입자입니다.";
      response.setCompanyId(null);
    }

    response.setValidBusinessNumber(validBusinessNumber);
    response.setAlreadyRegistered(alreadyRegistered);
    response.setMessage(msg);
    return response;
  }

  //신규 회사 등록
  public CompanyResponse createCompany(CompanyRequest dto) {
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

  // 사용자의 회사 정보 조회
  public CompanyResponse getCompanyInfo(Long companyId) {
    Company company = companyMapper.findById(companyId);
    if (company == null) {
      throw new IllegalArgumentException("해당 기업이 존재하지 않습니다.");
    }
    return new CompanyResponse(company);
  }
}