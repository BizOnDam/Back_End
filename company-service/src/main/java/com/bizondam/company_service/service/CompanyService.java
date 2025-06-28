package com.bizondam.company_service.service;

import com.bizondam.common.exception.CustomException;
import com.bizondam.company_service.dto.CompanyResponse;
import com.bizondam.company_service.dto.CompanyValidateResultResponse;
import com.bizondam.company_service.dto.StaffUpdateRequest;
import com.bizondam.company_service.entity.Company;
import com.bizondam.company_service.dto.CompanyRequest;
import com.bizondam.company_service.dto.CompanyValidationRequest;
import com.bizondam.company_service.entity.CompanyUser;
import com.bizondam.company_service.exception.CompanyErrorCode;
import com.bizondam.company_service.mapper.CompanyMapper;
import com.bizondam.company_service.client.NationalTaxClient;
import com.bizondam.company_service.mapper.CompanyUserMapper;
import com.bizondam.company_service.entity.CompanyUserEntity;
import java.util.stream.Collectors;
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
  private final CompanyUserMapper companyUserMapper;
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

  // 사용자의 회사 정보 조회
  public CompanyResponse getCompanyInfo(Long companyId) {
    Company company = companyMapper.findById(companyId);
    if (company == null) {
      throw new IllegalArgumentException("해당 기업이 존재하지 않습니다.");
    }
    return new CompanyResponse(company);
  }

  // 회사 내 모든 직원 조회
  public List<CompanyUser> getUsersInCompany(String userRole, Long companyId) {
    validateCeo(userRole);

    List<CompanyUserEntity> users = companyUserMapper.selectUsersByCompany(companyId);

    return users.stream()
        .map(CompanyUser::fromEntity)
        .collect(Collectors.toList());
  }

  // 특정 직원 상세 조회
  public CompanyUser getUserDetail(String requesterRole, Long companyId, Long targetUserId) {
    validateCeo(requesterRole);
    CompanyUserEntity user = companyUserMapper.selectUserByIdAndCompany(targetUserId, companyId);
    if (user == null || Boolean.TRUE.equals(user.isDeleted())) {
      throw new CustomException(CompanyErrorCode.USER_NOT_FOUND);
    }
    return CompanyUser.fromEntity(user);
  }

  // 특정 직원 삭제
  public void deleteStaff(Long requesterId, String userRole, Long companyId, Long targetUserId) {
    validateCeo(userRole);

    // 자기 자신 삭제 방지
    if (requesterId.equals(targetUserId)) {
      throw new CustomException(CompanyErrorCode.USER_CANNOT_DELETE_SELF);
    }

    // 직원 존재 여부 확인
    CompanyUserEntity target = companyUserMapper.selectUserByIdAndCompany(targetUserId, companyId);
    if (target == null) {
      throw new CustomException(CompanyErrorCode.USER_NOT_FOUND);
    }

    // 이미 삭제된 경우
    if (target.isDeleted()) {
      throw new CustomException(CompanyErrorCode.USER_ALREADY_DELETED);
    }

    // CEO는 삭제 불가
    if ("CEO".equalsIgnoreCase(target.getRoleInCompany())) {
      throw new CustomException(CompanyErrorCode.USER_CANNOT_DELETE_CEO);
    }

    companyUserMapper.deleteStaff(targetUserId);
  }

  // 직원 정보 수정
  public void updateStaff(Long requesterId, String userRole, Long companyId, Long targetUserId, StaffUpdateRequest request) {
    validateCeo(userRole);

    CompanyUserEntity target = companyUserMapper.selectUserByIdAndCompany(targetUserId, companyId);
    if (target == null || target.isDeleted()) {
      throw new CustomException(CompanyErrorCode.USER_NOT_FOUND);
    }

    companyUserMapper.updateStaffInfo(targetUserId, request.getDepartment(), request.getPosition(), request.getRoleDesc(), LocalDateTime.now());
  }

  public void transferCeoRole(Long requesterId, String userRole, Long companyId, Long targetUserId) {
    validateCeo(userRole);

    if (requesterId.equals(targetUserId)) {
      throw new CustomException(CompanyErrorCode.USER_CANNOT_TRANSFER_TO_SELF);
    }

    CompanyUserEntity requester = companyUserMapper.selectUserByIdAndCompany(requesterId, companyId);
    CompanyUserEntity target = companyUserMapper.selectUserByIdAndCompany(targetUserId, companyId);

    if (requester == null || target == null) {
      throw new CustomException(CompanyErrorCode.USER_NOT_FOUND);
    }

    if (requester.isDeleted() || target.isDeleted()) {
      throw new CustomException(CompanyErrorCode.USER_ALREADY_DELETED);
    }

    if (!"CEO".equalsIgnoreCase(requester.getRoleInCompany())) {
      throw new CustomException(CompanyErrorCode.USER_NOT_CEO);
    }

    if (!"STAFF".equalsIgnoreCase(target.getRoleInCompany())) {
      throw new CustomException(CompanyErrorCode.USER_TRANSFER_TARGET_MUST_BE_STAFF);
    }

    // 트랜잭션 내에서 두 사람의 role_in_company를 교환
    companyUserMapper.updateRoleInCompany(requesterId, "STAFF", LocalDateTime.now());
    companyUserMapper.updateRoleInCompany(targetUserId, "CEO", LocalDateTime.now());
  }

  // CEO 권한 검증
  private void validateCeo(String userRole) {
    if (!"CEO".equalsIgnoreCase(userRole)) {
      throw new CustomException(CompanyErrorCode.USER_NOT_CEO);
    }
  }
}