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
@Table(name = "product_meta")
public class ProductMeta {

    @Id
    @Column(length = 20)
    private String productId; // 물품 식별번호

    @Column(nullable = false, length = 255)
    private String productName; // 물품규격명

    @Column(length = 10)
    private String categoryCode; // 물품분류번호
    @Column(length = 100)
    private String categoryName; // 품명

    @Column(length = 10)
    private String detailCategoryCode; // 세부분류번호
    @Column(length = 100)
    private String detailCategoryName; // 세부품명

    @Column(length = 255)
    private String specification; // 물품규격 상세 설명

    private LocalDateTime createdAt;
}