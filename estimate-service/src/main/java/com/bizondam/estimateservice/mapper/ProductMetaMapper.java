package com.bizondam.estimateservice.mapper;

import com.bizondam.estimateservice.dto.ProductDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMetaMapper {

    List<String> selectDistinctCategoryNames();

    List<ProductDetailDto> selectDetailByCategoryName(@Param("categoryName") String categoryName);
}