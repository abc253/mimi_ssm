package com.li.service;

import com.github.pagehelper.PageInfo;
import com.li.domain.ProductInfo;
import com.li.domain.vo.ProductInfoVo;

import java.util.List;

public interface ProductInfoService {
    PageInfo splitPage(int pageNum, int pageSize);

    int save(ProductInfo productInfo);

    ProductInfo getByID(Integer pid);

    int update(ProductInfo productInfo);

    int delete(Integer pid);

    int deleteBatch(String[] ids);

    List<ProductInfo> selectCondition(ProductInfoVo productInfoVo);

    PageInfo splitPageVo(ProductInfoVo productInfoVo, int pageSize);
}
