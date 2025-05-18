package com.bizondam.publicdata_service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcurementHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId; // 납품 이력 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private ProcurementContract contract; // 조달 계약 요청

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductMeta product; // 물품 정보

    @Column(length = 20)
    private String supplierBizno; // 업체 사업자등록번호

    @Column(length = 100)
    private String supplierName; // 업체명

    @Column(length = 50)
    private String supplierType; // 업체 기업구분명

    private int unitPrice; // 단가
    private int quantity; // 수량

    @Column(length = 10)
    private String unit; // 단위

    private int totalAmount; // 총 금액

    private int increaseQuantity; // 증감수량
    private int increaseAmount; // 증감금액

    @Column(length = 5)
    private String finalChangeOrder; // 최종변경차수여부

    @Column(length = 10)
    private String changeOrder; // 변경차수

    @Column(length = 1)
    private String excellentProductYn; // 우수제품 여부

    @Column(length = 1)
    private String directPurchaseYn; // 공사용자재 직접구매대상 여부

    @Column(length = 1)
    private String masYn; // 다수공급자 계약 여부

    @Column(length = 1)
    private String masTwoStepYn; // 다수공급자 계약 2단계 여부

    @Column(length = 50)
    private String unitContractNo; // 단가계약번호

    @Column(length = 10)
    private String unitContractChangeOrder; // 단가계약 변경차수

    @Column(length = 100)
    private String deliveryCondition; // 인도조건명

    private LocalDateTime createdAt; // 등록 일시
}
