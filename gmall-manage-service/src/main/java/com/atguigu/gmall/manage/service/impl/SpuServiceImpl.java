package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsProductSaleAttrValue;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author lxc
 * @date 2020/3/28 21:11
 */

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    private PmsProductImageMapper pmsProductImageMapper;

    @Autowired
    private PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    private PmsProductSaleAttrValueMApper pmsProductSaleAttrValueMApper;


    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        return pmsProductInfoMapper.select(pmsProductInfo);
    }

    @Override
    public List<PmsProductImage> spuImageList(String productId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(productId);
        return pmsProductImageMapper.select(pmsProductImage);
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String productId) {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(productId);
        return pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
    }

    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {
        pmsProductInfoMapper.insert(pmsProductInfo);   //spu信息


        List<PmsProductImage> pmsProductImageList = pmsProductInfo.getPmsProductImageList();
        for (PmsProductImage pmsProductImage:pmsProductImageList) {
             pmsProductImage.setProductId(pmsProductInfo.getId());
            pmsProductImageMapper.insert(pmsProductImage);
        }

        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductInfo.getPmsProductSaleAttrList();
        for (PmsProductSaleAttr pmsProductSaleAttr:pmsProductSaleAttrList) {
            pmsProductSaleAttr.setProductId(pmsProductInfo.getId());
            pmsProductSaleAttrMapper.insert(pmsProductSaleAttr);

            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttr.getPmsProductSaleAttrValueList();
            for (PmsProductSaleAttrValue p: pmsProductSaleAttrValueList) {
                p.setProductId(pmsProductInfo.getId());
                pmsProductSaleAttrValueMApper.insert(p);
            }

        }

    }
}
