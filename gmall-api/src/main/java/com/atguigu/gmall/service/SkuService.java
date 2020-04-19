package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsSkuInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lxc
 * @date 2020/4/5 22:27
 */
public interface SkuService {
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    public PmsSkuInfo selectPmsSkuInfoById(String skuId);

    public List<PmsSkuInfo> selectSkuSaleAttrValueListBySpu(@Param("productId") String productId);
}
