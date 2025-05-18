package com.bizondam.publicdata_service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcurementContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;

    @Column(nullable = false, unique = true, length = 50)
    private String contractRequestNo; // 계약납품요구번호

    @Column(length = 255)
    private String contractName; // 계약명

    @Column(length = 50)
    private String contractMethod; // 계약체결방법명

    @Column(length = 50)
    private String contractDivision; // 계약구분명

    @Column(length = 50)
    private String procurementDivision; // 조달구분명

    @Column(length = 50)
    private String deliveryDivision; // 계약납품구분명

    private LocalDate contractDate; // 계약납품요구일자

    private LocalDate firstContractDate; // 최초계약납품요구일자

    private LocalDate deliveryDeadline; // 납품기한일자

    @Column(length = 255)
    private String deliveryPlace; // 납품장소명

    @Column(length = 100)
    private String institutionName; // 수요기관명

    @Column(length = 50)
    private String institutionType; // 수요기관구분명

    @Column(length = 100)
    private String regionName; // 수요기관지역명

    @Column(length = 20)
    private String institutionCode; // 수요기관코드

    private LocalDateTime createdAt;
}
