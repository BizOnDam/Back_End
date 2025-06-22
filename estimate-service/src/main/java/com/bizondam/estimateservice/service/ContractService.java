package com.bizondam.estimateservice.service;

import com.bizondam.common.exception.CustomException;
import com.bizondam.estimateservice.dto.ContractDto;
import com.bizondam.estimateservice.dto.ContractHistoryDto;
import com.bizondam.estimateservice.dto.ContractItemDto;
import com.bizondam.estimateservice.dto.ContractListResponseDto;
import com.bizondam.estimateservice.exception.EstimateErrorCode;
import com.bizondam.estimateservice.mapper.ContractMapper;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractService {
  private final ContractMapper contractMapper;

  public ContractDto getContract(Long requestId, Long responseId) {
    ContractDto dto = contractMapper.findContractByRequestIdAndResponseId(requestId, responseId);
    if (dto == null) {
      throw new CustomException(EstimateErrorCode.ESTIMATE_NOT_FOUND);
    }
    // 품목 리스트 조회 및 세팅
    List<ContractItemDto> items = contractMapper.findContractItemsByRequestIdAndResponseId(requestId, responseId);
    dto.setItems(items);
    return dto;
  }

  // 견적 요청서만 존재하는 경우
  public ContractDto getContractWithoutResponse(Long requestId) {
    ContractDto dto = contractMapper.findContractByRequestIdOnly(requestId);
    if (dto == null) {
      throw new CustomException(EstimateErrorCode.ESTIMATE_NOT_FOUND);
    }
    // items 채우기
    List<ContractItemDto> items = contractMapper.findContractItemsByRequestIdOnly(requestId);
    dto.setItems(items);
    return dto;
  }

  // 수요 업체용
  public List<ContractDto> getRequestsForBuyer(Long companyId, String userRole, Long userId) {
    List<ContractDto> list;
    if ("CEO".equalsIgnoreCase(userRole)) {
      // 회사의 모든 요청서
      list = contractMapper.selectContractsByBuyerCompany(companyId);
    } else {
      // STAFF: 본인이 생성한 요청서만
      list = contractMapper.selectContractsByBuyerCompanyAndUser(companyId, userId);
    }
    for (ContractDto dto : list) {
      List<ContractItemDto> items = contractMapper.findContractItemsByRequestIdAndResponseId(
          dto.getRequestId(), dto.getResponseId()
      );
      dto.setItems(items);
    }
    return list;
  }

  // 공급 업체용
  public List<ContractDto> getRequestsForSupplier(Long companyId, String userRole, Long userId) {
    List<ContractDto> list;
    if ("CEO".equalsIgnoreCase(userRole)) {
      // 회사의 모든 할당 요청서
      list = contractMapper.selectContractsBySupplierCompany(companyId);
    } else {
      // STAFF: 본인이 응답한 요청서만
      list = contractMapper.selectContractsBySupplierCompanyAndUser(companyId, userId);
    }
    for (ContractDto dto : list) {
      List<ContractItemDto> items = contractMapper.findContractItemsByRequestIdAndResponseId(
          dto.getRequestId(), dto.getResponseId()
      );
      dto.setItems(items);
    }
    return list;
  }

  // 계약 리스트
  public List<ContractListResponseDto> getContractList(
      Long userId, Long companyId, String role, String userRole, LocalDate date) {

    List<ContractListResponseDto> contracts;

    // CEO: 회사의 모든 계약
    if ("CEO".equalsIgnoreCase(userRole)) {
      if ("BUYER".equalsIgnoreCase(role)) {
        contracts = contractMapper.findContractsByBuyer(companyId, null); // userId null
      } else {
        contracts = contractMapper.findContractsBySupplier(companyId, null); // userId null
      }
    }
    // STAFF: 본인이 요청/응답한 계약만
    else {
      if ("BUYER".equalsIgnoreCase(role)) {
        contracts = contractMapper.findContractsByBuyer(companyId, userId); // userId 필터
      } else {
        contracts = contractMapper.findContractsBySupplier(companyId, userId); // userId 필터
      }
    }

    // 이하 품목 및 날짜 필터링 로직 동일
    for (ContractListResponseDto dto : contracts) {
      List<ContractItemDto> items = contractMapper.findContractItemsByRequestIdAndResponseId(
          dto.getRequestId(), dto.getResponseId());
      List<String> itemNames = items.stream()
          .map(ContractItemDto::getDetailCategoryName)
          .collect(Collectors.toList());
      dto.setItemNames(itemNames);
    }

    List<ContractListResponseDto> filtered = contracts.stream()
        .filter(dto -> {
          if (date == null) return true;
          return date.equals(dto.getContractDate()) || date.equals(dto.getDueDate());
        })
        .sorted(Comparator.comparing(ContractListResponseDto::getContractDate).reversed())
        .collect(Collectors.toList());

    return filtered;
  }

  public List<ContractHistoryDto> getContractHistoryByCompanyId(Long companyId) {
    return contractMapper.selectContractHistoryByCompanyId(companyId);
  }
}