package com.li.service.impl;

import com.li.domain.ProductType;
import com.li.domain.ProductTypeExample;
import com.li.mapper.ProductTypeMapper;
import com.li.service.ProductTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {
    @Resource
    private ProductTypeMapper productTypeMapper;

    @Override
    public List<ProductType> getProductTypes() {
        return productTypeMapper.selectByExample(new ProductTypeExample());
    }
}
