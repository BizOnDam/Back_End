package com.bizondam.estimateservice.service;

import com.bizondam.estimateservice.dto.ProductDetailDto;
import com.bizondam.estimateservice.mapper.ProductMetaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductMetaService {

    private final ProductMetaMapper productMetaMapper;

    public List<String> getCategoryNames() {
        return productMetaMapper.selectDistinctCategoryNames();
    }

    public List<ProductDetailDto> getDetailList(String categoryName) {
        return productMetaMapper.selectDetailByCategoryName(categoryName);
    }

}
