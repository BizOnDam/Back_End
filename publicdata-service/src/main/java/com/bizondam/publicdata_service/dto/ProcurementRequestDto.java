package com.bizondam.publicdata_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcurementRequestDto {

    // 필수 파라미터
    private int numOfRows;          // 한 페이지 결과 수
    private int pageNo;             // 페이지 번호
    private String inqryDiv;        // 조회구분 (1: 계약일자 기준, 2: 최초계약일자 기준)
    private String inqryBgnDate;    // 조회 시작일자 (YYYYMMDD)
    private String inqryEndDate;    // 조회 종료일자 (YYYYMMDD)
    private String inqryPrdctDiv;   // 조회물품구분 (1: 품명, 2: 세부품명, 3: 물품규격명)

    // 조건부 필수 (조회물품구분에 따라 선택적으로 입력)
    private String prdctClsfcNo;        // 물품분류번호 (8자리)
    private String prdctClsfcNoNm;      // 품명
    private String dtilPrdctClsfcNo;    // 세부품명번호
    private String dtilPrdctClsfcNoNm;  // 세부품명
    private String prdctIdntNo;         // 물품식별번호
    private String prdctIdntNoNm;       // 물품규격명

    // 선택 입력 (필터링용)
    private String prcrmntDiv;          // 조달구분 (C:중앙조달, S:자체조달)
    private String dminsttcD;           // 수요기관코드
    private String dminsttNm;           // 수요기관명
    private String bizRegNo;            // 업체사업자번호
    private String corpNm;              // 업체명
    private String fnlCntrctDlvrReqChgOrdYn; // 최종변경차수여부 (Y/N)
    private String exclcProdctYn;       // 우수제품여부
    private String cnstwkMtrlDrctPurchsObjYn; // 공사용자재직접구매대상여부
}
