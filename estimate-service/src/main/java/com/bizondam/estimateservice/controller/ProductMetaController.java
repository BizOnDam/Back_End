package com.bizondam.estimateservice.controller;

import com.bizondam.common.response.BaseResponse;
import com.bizondam.estimateservice.dto.ProductDetailDto;
import com.bizondam.estimateservice.service.ProductMetaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product-meta")
@RequiredArgsConstructor
public class ProductMetaController {

    private final ProductMetaService productMetaService;

    @Operation(summary = "카테고리 목록 조회", description = "중복 없이 전체 카테고리명(category_name) 목록 반환")
    @GetMapping("/categories")
    public ResponseEntity<BaseResponse<List<String>>> getCategoryNames() {
        List<String> categories = productMetaService.getCategoryNames();
        return ResponseEntity.ok(BaseResponse.success("카테고리 목록 조회 성공", categories));
    }

    @Operation(summary = "세부 카테고리 조회", description = "선택한 카테고리(category_name)에 해당하는 세부품명 목록 반환")
    @GetMapping("/details")
    public ResponseEntity<BaseResponse<List<ProductDetailDto>>> getDetailCategories(
            @RequestParam("category") String categoryName) {
        List<ProductDetailDto> details = productMetaService.getDetailList(categoryName);
        return ResponseEntity.ok(BaseResponse.success("세부 카테고리 조회 성공", details));
    }
}