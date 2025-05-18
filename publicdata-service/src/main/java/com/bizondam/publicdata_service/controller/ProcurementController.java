package com.bizondam.publicdata_service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.bizondam.publicdata_service.dto.*;
import com.bizondam.publicdata_service.service.ProcurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "조달청 API")
@RestController
@RequestMapping("/api/procurements")
@RequiredArgsConstructor
public class ProcurementController {

    private final ProcurementService service;

    // 전체 동기화
    @PostMapping("/sync")
    public ResponseEntity<Void> sync(@RequestBody ProcurementRequestDto req) {
        service.syncProcurements(req);
        return ResponseEntity.ok().build();
    }

    // 품목 메타 조회
    @Tag(name = "물품정보", description = "쇼핑몰 품목 메타정보 조회·관리 API")
    @GetMapping("/metas")
    public List<ProductMetaDto> getMetas() {
        return service.getAllProductMetas();
    }

    // 계약 요청 조회
    @GetMapping("/contracts")
    public List<ProcurementContractDto> getContracts() {
        return service.getAllContracts();
    }

    // 원본 이력 전체 조회
    @GetMapping("/histories/all")
    public List<ProcurementHistoryDto> getAllHistories() {
        return service.getAllHistories();
    }

    // 특정 물품 납품 이력 조회
    @GetMapping("/histories")
    public List<SupplierCountDto> getHistories(@RequestParam String productId) {
        return service.getSupplierCounts(productId);
    }

}
