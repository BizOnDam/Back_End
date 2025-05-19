package com.bizondam.publicdata_service.dto;

import com.bizondam.publicdata_service.domain.ProcurementHistory;
import lombok.*;

@Getter
@Builder
public class ProcurementHistoryDto {
    private Long historyId;
    private String contractRequestNo;          // 계약납품요구번호
    private String productId;                  // 물품식별번호
    private String supplierBizno;              // 업체 사업자등록번호
    private String supplierName;               // 업체명
    private String supplierType;               // 업체기업구분명
    private int unitPrice;                     // 단가
    private int quantity;                      // 수량
    private String unit;                       // 단위
    private int totalAmount;                   // 금액
    private int increaseQuantity;              // 증감수량
    private int increaseAmount;                // 증감금액
    private String finalChangeOrder;           // 최종변경차수여부
    private String changeOrder;                // 변경차수
    private String excellentProductYn;         // 우수제품여부
    private String directPurchaseYn;           // 공사용자재직접구매대상여부
    private String masYn;                      // 다수공급자계약여부
    private String masTwoStepYn;               // 다수공급자계약2단계진행여부
    private String unitContractNo;             // 단가계약번호
    private String unitContractChangeOrder;    // 단가계약변경차수
    private String deliveryCondition;          // 인도조건명

    public static ProcurementHistoryDto from(ProcurementHistory h) {
        return ProcurementHistoryDto.builder()
                .historyId(h.getHistoryId())
                .contractRequestNo(h.getContract().getContractRequestNo())
                .productId(h.getProduct().getProductId())
                .supplierBizno(h.getSupplierBizno())
                .supplierName(h.getSupplierName())
                .supplierType(h.getSupplierType())
                .unitPrice(h.getUnitPrice())
                .quantity(h.getQuantity())
                .unit(h.getUnit())
                .totalAmount(h.getTotalAmount())
                .increaseQuantity(h.getIncreaseQuantity())
                .increaseAmount(h.getIncreaseAmount())
                .finalChangeOrder(h.getFinalChangeOrder())
                .changeOrder(h.getChangeOrder())
                .excellentProductYn(h.getExcellentProductYn())
                .directPurchaseYn(h.getDirectPurchaseYn())
                .masYn(h.getMasYn())
                .masTwoStepYn(h.getMasTwoStepYn())
                .unitContractNo(h.getUnitContractNo())
                .unitContractChangeOrder(h.getUnitContractChangeOrder())
                .deliveryCondition(h.getDeliveryCondition())
                .build();
    }
}
