package com.bizondam.estimateservice.service;

import com.bizondam.common.exception.CustomException;
import com.bizondam.estimateservice.dto.ContractDto;
import com.bizondam.estimateservice.dto.ContractItemDto;
import com.bizondam.estimateservice.dto.ContractListResponse;
import com.bizondam.estimateservice.exception.EstimateErrorCode;
import com.bizondam.estimateservice.mapper.ContractMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

  public List<ContractDto> getRequestsForBuyer(Long companyId) {
    List<ContractDto> list = contractMapper.selectContractsByBuyerCompany(companyId);
    // items 채우기
    for (ContractDto dto : list) {
      List<ContractItemDto> items = contractMapper.findContractItemsByRequestIdAndResponseId(
              dto.getRequestId(), dto.getResponseId()
      );
      dto.setItems(items);
    }
    return list;
  }

  public List<ContractDto> getRequestsForSupplier(Long companyId) {
    List<ContractDto> list = contractMapper.selectContractsBySupplierCompany(companyId);
    // items 채우기
    for (ContractDto dto : list) {
      List<ContractItemDto> items = contractMapper.findContractItemsByRequestIdAndResponseId(
              dto.getRequestId(), dto.getResponseId()
      );
      dto.setItems(items);
    }
    return list;
  }


  public List<ContractListResponse> getContractList(Long userId, Long companyId, String role, String date) {
    List<ContractListResponse> contracts;

    System.err.println("입력값: userId=" + userId + ", companyId=" + companyId + ", role=" + role + ", date=" + date);

    if ("BUYER".equalsIgnoreCase(role)) {
      contracts = contractMapper.findContractsByBuyer(companyId, userId);
      System.err.println("BUYER용 계약 개수: " + contracts.size());
    } else {
      contracts = contractMapper.findContractsBySupplier(companyId, userId);
      System.err.println("SUPPLIER용 계약 개수: " + contracts.size());
    }

    for (ContractListResponse dto : contracts) {
      System.err.println("계약 ID: " + dto.getContractId()
              + " | 요청 ID: " + dto.getRequestId()
              + " | 응답 ID: " + dto.getResponseId()
              + " | 계약일자: " + dto.getContractDate()
              + " | 납품기한: " + dto.getDueDate());

      List<ContractItemDto> items = contractMapper.findContractItemsByRequestIdAndResponseId(
              dto.getRequestId(), dto.getResponseId());

      System.err.println("품목 개수: " + items.size());
      for (ContractItemDto item : items) {
        System.err.println("품목명: " + item.getDetailCategoryName());
      }

      List<String> itemNames = items.stream()
              .map(ContractItemDto::getDetailCategoryName)
              .collect(Collectors.toList());

      dto.setItemNames(itemNames);
    }

    List<ContractListResponse> filtered = contracts.stream()
            .filter(dto -> {
              if (date == null) return true;
              boolean matches = date.equals(dto.getContractDate()) || date.equals(dto.getDueDate());
              System.err.println("필터링 확인 - 계약ID: " + dto.getContractId() + ", 포함여부: " + matches);
              return matches;
            })
            .sorted(Comparator.comparing(ContractListResponse::getContractDate).reversed())
            .collect(Collectors.toList());

    System.err.println("최종 반환 개수: " + filtered.size());
    return filtered;
  }
}