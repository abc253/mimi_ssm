package com.li.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.li.domain.ProductInfo;
import com.li.domain.ProductInfoExample;
import com.li.domain.vo.ProductInfoVo;
import com.li.mapper.ProductInfoMapper;
import com.li.service.ProductInfoService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    @Resource
    ProductInfoMapper productInfoMapper;


     /*
        执行分页操作
     */
    @Override
    public PageInfo splitPage(int pageNum, int pageSize) {
        //完成PageHelper的分页设置
        PageHelper.startPage(pageNum,pageSize);

        //进行有条件的查询操作,因此创建ProductInfoExampled对象
        ProductInfoExample example = new ProductInfoExample();
        //设置排序，按照主键降序排序
        //select * from product_info ORDER BY p_id desc;
        example.setOrderByClause("p_id desc");
        //执行降序的条件查询操作
        List<ProductInfo> productInfos = productInfoMapper.selectByExample(example);
        //把结果集封装到PageInfo中,并返回分页后的数据
        PageInfo<ProductInfo> info = new PageInfo<>(productInfos);
        return info;
    }

    /*
        添加商品
    */
    @Override
    public int save(ProductInfo productInfo) {
        return productInfoMapper.insert(productInfo);
    }

    /*
        根据主键查询商品信息
    */
    @Override
    public ProductInfo getByID(Integer pid) {
        return productInfoMapper.selectByPrimaryKey(pid);
    }

    /*
        根据主键修改商品信息
    */
    @Override
    public int update(ProductInfo productInfo) {
        return productInfoMapper.updateByPrimaryKey(productInfo);
    }

    /*
        根据主键删除商品信息
    */
    @Override
    public int delete(Integer pid) {
        return productInfoMapper.deleteByPrimaryKey(pid);
    }


    /*
            根据主键批量删除商品信息
        */
    @Override
    public int deleteBatch(String[] ids) {
        return productInfoMapper.deleteBatch(ids);
    }

    /*
        多条件的查询
    */
    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo productInfoVo) {
        return productInfoMapper.selectCondition(productInfoVo);
    }

    /*
        多条件的查询分页
    */
    @Override
    public PageInfo splitPageVo(ProductInfoVo productInfoVo, int pageSize) {
        //设置PageHepler.startPage属性
        PageHelper.startPage(productInfoVo.getPage(),pageSize);
        List<ProductInfo> productInfos = productInfoMapper.selectCondition(productInfoVo);
        return new PageInfo(productInfos);
    }
}
