package com.bizondam.publicdata_service.mapper;

import com.bizondam.publicdata_service.domain.ProductMeta;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMetaMapper {

    // XML <insert id="upsert">에 매핑
    void upsert(ProductMeta pm);

     // XML <select id="selectById">에 매핑
    ProductMeta selectById(String productId);

    List<ProductMeta> selectAll();
}
