package com.bizondam.estimateservice.mapper;

import com.bizondam.estimateservice.dto.ContractDto;
import com.bizondam.estimateservice.dto.ContractItemDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ContractMapper {
  // 계약서 단일 객체 조회
  ContractDto findContractByRequestIdAndResponseId(@Param("requestId") Long requestId, @Param("responseId") Long responseId);
  // 품목 리스트 조회
  List<ContractItemDto> findContractItemsByRequestIdAndResponseId(@Param("requestId") Long requestId, @Param("responseId") Long responseId);
}