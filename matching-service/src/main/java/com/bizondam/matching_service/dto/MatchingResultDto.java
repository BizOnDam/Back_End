package com.bizondam.matching_service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchingResultDto {
    String summary;
    private final List<SupplierDto> commonSuppliers;
    private final Map<Long, List<SupplierDto>> perItemSuppliers;

    public static MatchingResultDto common(String summary, List<SupplierDto> common) {
        return MatchingResultDto.builder()
                .summary(summary)
                .commonSuppliers(common)
                .perItemSuppliers(Collections.emptyMap())
                .build();
    }

    public static MatchingResultDto perItem(String summary, Map<Long, List<SupplierDto>> perItem) {
        return MatchingResultDto.builder()
                .summary(summary)
                .commonSuppliers(Collections.emptyList())
                .perItemSuppliers(perItem)
                .build();
    }
}
