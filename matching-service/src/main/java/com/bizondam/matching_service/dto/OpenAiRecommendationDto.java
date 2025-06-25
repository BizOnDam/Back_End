package com.bizondam.matching_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiRecommendationDto {
    private String summary;
    private List<SupplierDto> commonSuppliers;
    private Map<String, List<SupplierDto>> perItemSuppliers;
}
