package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.PmsSkuInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author lxc
 * @date 2020/4/5 22:48
 */
public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo> {
    public List<PmsSkuInfo> selectSkuSaleAttrValueListBySpu(@Param("productId") String productId);
}
