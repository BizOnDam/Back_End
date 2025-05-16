package com.bizondam.publicdata_service.dto;

import lombok.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcurementSyncResponseDto {

    private List<ProductMetaDto>             productMetas;  // 품목 메타정보
    private List<ProcurementContractDto>     contracts;     // 조달 계약 요청 정보
    private List<ProcurementHistoryDto>      histories;     // 조달 납품 이력
}
