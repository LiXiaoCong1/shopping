package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;

import java.util.List;

/**
 * @author lxc
 * @date 2020/3/28 21:08
 */
public interface SpuService {

    public List<PmsProductInfo> spuList(String catalog3Id );

    public List<PmsProductImage> spuImageList(String spuId);

    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    public void saveSpuInfo(PmsProductInfo pmsProductInfo);
}
