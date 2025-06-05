package com.bizondam.publicdata_service.dto;

import com.bizondam.publicdata_service.domain.ProcurementContract;
import lombok.*;
import java.time.LocalDate;

@Getter
@Builder
public class ProcurementContractDto {
    private Long contractId;
    private String contractRequestNo;      // 계약납품요구번호
    private String contractName;           // 계약명
    private String contractMethod;         // 계약체결방법명
    private String contractDivision;       // 계약구분명
    private String procurementDivision;    // 조달구분명
    private String deliveryDivision;       // 계약납품구분명
    private LocalDate contractDate;        // 계약납품요구일자
    private LocalDate firstContractDate;   // 최초계약납품요구일자
    private LocalDate deliveryDeadline;    // 납품기한일자
    private String deliveryPlace;          // 납품장소명
    private String institutionName;        // 수요기관명
    private String institutionType;        // 수요기관구분명
    private String regionName;             // 수요기관지역명
    private String institutionCode;        // 수요기관코드

    public static ProcurementContractDto from(ProcurementContract e) {
        return ProcurementContractDto.builder()
                .contractId(e.getContractId())
                .contractRequestNo(e.getContractRequestNo())
                .contractName(e.getContractName())
                .contractMethod(e.getContractMethod())
                .contractDivision(e.getContractDivision())
                .procurementDivision(e.getProcurementDivision())
                .deliveryDivision(e.getDeliveryDivision())
                .contractDate(e.getContractDate())
                .firstContractDate(e.getFirstContractDate() != null ? e.getFirstContractDate() : null)
                .deliveryDeadline(e.getDeliveryDeadline() != null ? e.getDeliveryDeadline() : null)
                .deliveryPlace(e.getDeliveryPlace())
                .institutionName(e.getInstitutionName())
                .institutionType(e.getInstitutionType())
                .regionName(e.getRegionName())
                .institutionCode(e.getInstitutionCode())
                .build();
    }
}
