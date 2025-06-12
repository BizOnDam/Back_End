package com.bizondam.estimateservice.mapper;

import com.bizondam.estimateservice.dto.EstimateResponseCreateDto;
import com.bizondam.estimateservice.dto.EstimateResponseItemDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EstimateResponseMapper {
  // 1) estimate_response 헤더(메타) INSERT 후 자동 생성된 response_id를 dto나 파라미터에 세팅해야 함
  void insertEstimateResponse(EstimateResponseCreateDto dto);
  // 2) 2) estimate_response_item 테이블에 각 품목별 단가 INSERT
  void insertEstimateResponseItem(@Param("responseId") Long responseId,
      @Param("item") EstimateResponseItemDto item);
  // 3) 계약 체결/미체결 시, estimate_response.status UPDATE
  void updateResponseStatus(@Param("responseId") Long responseId,
      @Param("status") Integer status);
  // 4) requestId로 responseId 검색
  Long findResponseIdByRequestId(@Param("requestId") Long requestId);
}