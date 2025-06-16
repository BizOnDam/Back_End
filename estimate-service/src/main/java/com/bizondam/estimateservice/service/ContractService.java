package com.bizondam.estimateservice.service;

import com.bizondam.common.exception.CustomException;
import com.bizondam.estimateservice.dto.ContractDto;
import com.bizondam.estimateservice.dto.ContractItemDto;
import com.bizondam.estimateservice.exception.EstimateErrorCode;
import com.bizondam.estimateservice.mapper.ContractMapper;
import java.util.List;
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
}