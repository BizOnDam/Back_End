package com.bizondam.publicdata_service.service;

//import com.bizondam.publicdata_service.client.ProcurementClient;
//import com.bizondam.publicdata_service.domain.ProcurementContract;
//import com.bizondam.publicdata_service.domain.ProcurementHistory;
//import com.bizondam.publicdata_service.domain.ProductMeta;
import com.bizondam.publicdata_service.dto.*;
import com.bizondam.publicdata_service.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcurementService {

//    private final ProcurementClient client;
    private final ProductMetaMapper          metaMapper;
    private final ProcurementContractMapper  contractMapper;
    private final ProcurementHistoryMapper   historyMapper;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.BASIC_ISO_DATE;

//    @Transactional
//    public void syncProcurements(ProcurementRequestDto req) {
//        // 1) OpenAPI 호출 & 저장
//        ProcurementResponseDto raw = client.fetchContracts(req);
//
//        if (raw == null
//            || raw.getBody() == null
//            || raw.getBody().getItems() == null
//            || raw.getBody().getItems().isEmpty()) {
//            log.warn("OpenAPI 응답 없음: {}", raw);
//            return;
//        }
//
//        raw.getBody().getItems().forEach(item -> {
//            // 1-1) product_meta upsert
//            ProductMeta pm = ProductMeta.builder()
//                    .productId(item.getPrdctIdntNo())
//                    .productName(item.getPrdctIdntNoNm())
//                    .categoryCode(item.getPrdctClsfcNo())
//                    .categoryName(item.getPrdctClsfcNoNm())
//                    .detailCategoryCode(item.getDtilPrdctClsfcNo())
//                    .detailCategoryName(item.getDtilPrdctClsfcNoNm())
//                    .specification(item.getPrdctIdntNoNm())
//                    .createdAt(LocalDateTime.now())
//                    .build();
//            metaMapper.upsert(pm);
//            if (pm.getProductId() == null) {
//                log.error("product_meta upsert 실패: {}", pm);
//                return;  // 혹은 예외 던지기
//            }
//
//            // 1-2) procurement_contract upsert
//            ProcurementContract pc = ProcurementContract.builder()
//                    .contractRequestNo(item.getCntrctDlvrReqNo())
//                    .contractName(item.getCntrctDlvrReqNm())
//                    .contractMethod(item.getCntrctMthdNm())
//                    .contractDivision(item.getCntrctDivNm())
//                    .procurementDivision(item.getPrcrmntDivNm())
//                    .deliveryDivision(item.getCntrctDlvrDivNm())
//                    .contractDate(parseDate(item.getCntrctDlvrReqDate()))
//                    .firstContractDate(parseDate(item.getIntlCntrctDlvrReqDate()))
//                    .deliveryDeadline(parseDate(item.getDlvrTmlmtDate()))
//                    .deliveryPlace(item.getDlvrPlceNm())
//                    .institutionName(item.getDminsttNm())
//                    .institutionType(item.getDmndInsttDivNm())
//                    .regionName(item.getDminsttRgnNm())
//                    .institutionCode(item.getDminsttCd())
//                    .createdAt(LocalDateTime.now())
//                    .build();
//            try {
//                log.debug("▶ upsert 대상 contractRequestNo: {}", pc.getContractRequestNo());
//                log.debug("▶ 계약 정보: {}", pc);
//                contractMapper.upsert(pc);
//                ProcurementContract saved = contractMapper.selectByRequestNo(pc.getContractRequestNo());
//                pc.setContractId(saved.getContractId());
//            } catch (Exception e) {
//                log.error("▶ procurement_contract upsert 실패: {}", pc);
//                log.error("▶ 예외 메시지: ", e);
//                return;
//            }
//            if (pc.getContractId() == null) {
//                log.error("▶ upsert 후 contractId가 null 입니다. {}", pc);
//            }
//
//            // 1-3) procurement_history insert
//            ProcurementHistory ph = ProcurementHistory.builder()
//                    .contract(pc)
//                    .product(pm)
//                    .supplierBizno(item.getBizno())
//                    .supplierName(item.getCorpNm())
//                    .supplierType(item.getCorpEntrprsDivNmNm())
//                    .unitPrice(parseInt(item.getPrdctUprc()))
//                    .quantity(Integer.parseInt(item.getPrdctQty()))
//                    .unit(item.getPrdctUnit())
//                    .totalAmount(Integer.parseInt(item.getPrdctAmt()))
//                    .increaseQuantity(Integer.parseInt(item.getIncdecQty()))
//                    .increaseAmount(Integer.parseInt(item.getIncdecAmt()))
//                    .finalChangeOrder(item.getFnlCntrctDlvrReqChgOrdYn())
//                    .changeOrder(item.getCntrctDlvrReqChgOrd())
//                    .excellentProductYn(item.getExclcProdctYn())
//                    .directPurchaseYn(item.getCnstwkMtrlDrctPurchsObjYn())
//                    .masYn(item.getMasYn())
//                    .masTwoStepYn(item.getMasCntrct2StepYn())
//                    .unitContractNo(item.getUprcCntrctNo())
//                    .unitContractChangeOrder(item.getUprcCntrctCngOrd())
//                    .deliveryCondition(item.getDlvryCndtnNm())
//                    .createdAt(LocalDateTime.now())
//                    .build();
//            historyMapper.insertHistory(ph);
//        });
//
//    }
//
//    // 안전한 날짜 파싱
//    private LocalDate parseDate(String dateStr) {
//        if (dateStr == null || dateStr.isBlank()) return null;
//        return LocalDate.parse(dateStr, DATE_FMT);
//    }

    //품목 메타정보만 조회
    public List<ProductMetaDto> getAllProductMetas() {
        return metaMapper.selectAll()
                .stream()
                .map(ProductMetaDto::from)
                .toList();
    }

    //계약 요청 정보만 조회
    public List<ProcurementContractDto> getAllContracts() {
        return contractMapper.selectAll()
                .stream()
                .map(ProcurementContractDto::from)
                .toList();
    }

    // 원본 이력 전체 조회
    public List<ProcurementHistoryDto> getAllHistories() {
        return historyMapper
                .selectAll()
                .stream()
                .map(ProcurementHistoryDto::from)
                .toList();
    }

    // 특정 물품(productId) 기준 납품 이력(공급사별 통계)만 조회
    public List<SupplierCountDto> getSupplierCounts(String productId) {
        return historyMapper.countBySupplierForProduct(productId)
                .stream()
                .map(SupplierCountDto::from)
                .toList();
    }

    // 문자열 숫자 → int 파싱(예외 시 0 반환)
    private int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }
}