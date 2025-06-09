package com.bizondam.estimateservice.service;


import com.bizondam.estimateservice.dto.EstimateRequestCreateDto;
import com.bizondam.estimateservice.dto.EstimateRequestItemDto;
import com.bizondam.estimateservice.dto.EstimateResponseCreateDto;
import com.bizondam.estimateservice.dto.EstimateResponseItemDto;
import com.bizondam.estimateservice.mapper.EstimateRequestMapper;
import com.bizondam.estimateservice.mapper.EstimateResponseMapper;
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
  // estimate_request INSERT → 자동 생성된 requestId를 dto.requestId에 세팅
  // estimate_request_item INSERT (items)
  @Transactional
  public Long createEstimate(EstimateRequestCreateDto dto) {
    // 1. estimate_request 헤더 INSERT
    requestMapper.insertEstimateRequest(dto);
    Long requestId = dto.getRequestId(); // MyBatis가 useGeneratedKeys로 세팅

    // 2. 각 품목 테이블 INSERT
    if (dto.getItems() != null) {
      for (EstimateRequestItemDto item : dto.getItems()) {
        requestMapper.insertEstimateRequestItem(requestId, item);
      }
    }
    return requestId;
  }

  // 2) 수요 업체에서 공급 업체 지정
  // estimate_request.supplier_company_id UPDATE
  @Transactional
  public void assignSupplier(Long requestId, Long supplierCompanyId) {
    requestMapper.updateSupplierCompany(requestId, supplierCompanyId);
  }

  // 3) 견적 응답 생성(공급 업체에서 답변)
  // estimate_response INSERT → 자동 생성된 responseId를 dto.responseId에 세팅
  // estimate_response_item INSERT (responseItems)
  @Transactional
  public Long createResponse(EstimateResponseCreateDto dto) {
    // 1) estimate_response 헤더 INSERT
    responseMapper.insertEstimateResponse(dto);
    Long responseId = dto.getResponseId();

    // 2) 각 품목별 단가 INSERT
    if (dto.getResponseItems() != null) {
      for (EstimateResponseItemDto item : dto.getResponseItems()) {
        responseMapper.insertEstimateResponseItem(responseId, item);
      }
    }
    return responseId;
  }

  //계약 체결
  @Transactional
  public void acceptContract(Long requestId, Long actingUserId) {
    // 1) estimate_request status = 3(계약체결)
    requestMapper.updateRequestStatus(requestId, 3);
    responseMapper.updateResponseStatus(requestId, 3);
  }

  //계약 미체결
  @Transactional
  public void rejectContract(Long requestId, Long actingUserId) {
    requestMapper.updateRequestStatus(requestId, 4);
    responseMapper.updateResponseStatus(requestId, 4);
  }
}