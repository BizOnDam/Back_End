package com.bizondam.estimateservice.service;

import com.bizondam.common.exception.CustomException;
import com.bizondam.estimateservice.dto.EstimateRequestCreateDto;
import com.bizondam.estimateservice.dto.EstimateRequestItemDto;
import com.bizondam.estimateservice.dto.EstimateResponseCreateDto;
import com.bizondam.estimateservice.exception.EstimateErrorCode;
import com.bizondam.estimateservice.mapper.EstimateRequestMapper;
import com.bizondam.estimateservice.mapper.EstimateResponseMapper;
import com.bizondam.estimateservice.model.EstimateRequestItem;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EstimateService {
  private final EstimateRequestMapper requestMapper;
  private final EstimateResponseMapper responseMapper;

  // 1) 견적 요청서 생성
  @Transactional
  public Long createEstimate(EstimateRequestCreateDto dto) {
    // 헤더 INSERT (requestId 자동 생성)
    requestMapper.insertEstimateRequest(dto);
    Long requestId = dto.getRequestId(); // useGeneratedKeys로 자동 세팅됨

    // 아이템 INSERT
    if (dto.getItems() != null) {
      dto.getItems().forEach(item ->
          requestMapper.insertEstimateRequestItem(requestId, item)
      );
    }
    return requestId;
  }

  // 2) 수요 업체에서 공급 업체 지정
  @Transactional
  public void assignSupplier(Long requestId, Long supplierCompanyId) {
    requestMapper.updateSupplierCompany(requestId, supplierCompanyId);
  }

  // 3) 견적 응답 생성(공급 업체에서 답변)
  @Transactional
  public Long createResponse(EstimateResponseCreateDto dto) {
    // estimate_response 헤더 INSERT (responseId 자동 생성)
    responseMapper.insertEstimateResponse(dto);
    Long responseId = dto.getResponseId(); // useGeneratedKeys로 자동 세팅됨

    // 각 품목별 단가 INSERT
    if (dto.getResponseItems() != null) {
      dto.getResponseItems().forEach(item ->
          responseMapper.insertEstimateResponseItem(responseId, item)
      );
    }

    requestMapper.updateRequestStatus(dto.getRequestId(), 1);

    return responseId;
  }

  // 4) 계약 체결
  @Transactional
  public void acceptContract(Long requestId) {
    requestMapper.updateRequestStatus(requestId, 3);

    Long responseId = responseMapper.findResponseIdByRequestId(requestId);
    if (responseId == null) {
      throw new CustomException(EstimateErrorCode.ESTIMATE_NOT_FOUND);
    }

    responseMapper.updateResponseStatus(responseId, 3);
  }

  // 5) 계약 미체결
  @Transactional
  public void rejectContract(Long requestId) {
    requestMapper.updateRequestStatus(requestId, 4);

    Long responseId = responseMapper.findResponseIdByRequestId(requestId);
    if (responseId == null) {
      throw new CustomException(EstimateErrorCode.ESTIMATE_NOT_FOUND);
    }

    responseMapper.updateResponseStatus(responseId, 4);
  }

  public List<EstimateRequestItem> createEstimateAndReturnItems(EstimateRequestCreateDto dto) {
    requestMapper.insertEstimateRequest(dto);
    Long requestId = dto.getRequestId();

    List<EstimateRequestItem> itemList = new ArrayList<>();
    if (dto.getItems() != null) {
      for (EstimateRequestItemDto itemDto : dto.getItems()) {
        EstimateRequestItem item = new EstimateRequestItem();
        item.setRequestId(requestId);
        item.setProductId(itemDto.getProductId());
        item.setSpecification(itemDto.getSpecification());
        item.setQuantity(itemDto.getQuantity());
        requestMapper.insertEstimateRequestItemWithReturnId(item); // itemId 자동 할당
        itemList.add(item);
      }
    }
    return itemList;
  }
}