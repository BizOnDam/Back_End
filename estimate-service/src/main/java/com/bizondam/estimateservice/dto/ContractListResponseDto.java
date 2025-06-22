package com.bizondam.estimateservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractListResponseDto {
    private Long contractId;
    private Long requestId;
    private Long responseId;
    private Long supplierCompanyId;
    private List<String> itemNames;
    private Long totalPrice;
    private String contractDate;
    private String dueDate;
}
