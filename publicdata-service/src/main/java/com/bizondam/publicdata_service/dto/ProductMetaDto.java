//package com.bizondam.publicdata_service.dto;
//
//import com.bizondam.publicdata_service.domain.ProductMeta;
//import lombok.*;
//
//@Getter
//@Setter
//@Builder
//public class ProductMetaDto {
//    private String productId;              // 물품식별번호
//    private String productName;            // 물품규격명
//    private String categoryCode;           // 물품분류번호
//    private String categoryName;           // 품명
//    private String detailCategoryCode;     // 세부물품분류번호
//    private String detailCategoryName;     // 세부품명
//    private String specification;          // 물품규격 상세명
//
//    public static ProductMetaDto from(ProductMeta e) {
//        return ProductMetaDto.builder()
//                .productId(e.getProductId())
//                .productName(e.getProductName())
//                .categoryCode(e.getCategoryCode())
//                .categoryName(e.getCategoryName())
//                .detailCategoryCode(e.getDetailCategoryCode())
//                .detailCategoryName(e.getDetailCategoryName())
//                .specification(e.getSpecification())
//                .build();
//    }
//}