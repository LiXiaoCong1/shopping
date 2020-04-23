package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.PmsProductImageMapper;
import com.atguigu.gmall.manage.mapper.PmsProductInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrValueMApper;
import com.atguigu.gmall.service.SpuService;
import com.atguigu.gmall.service.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
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

    @Autowired
    private RedisUtil redisUtil;

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
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        pmsProductSaleAttrs.stream().forEach(pmsProductSaleAttr1 -> {
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setSaleAttrId(pmsProductSaleAttr1.getSaleAttrId());
            pmsProductSaleAttrValue.setProductId(productId);
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttrValueMApper.select(pmsProductSaleAttrValue);
            pmsProductSaleAttr1.setPmsProductSaleAttrValueList(pmsProductSaleAttrValueList);
        });
        return pmsProductSaleAttrs;
    }

    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {
        pmsProductInfoMapper.insert(pmsProductInfo);   //spu信息

        List<PmsProductImage> pmsProductImageList = pmsProductInfo.getPmsProductImageList();
        pmsProductImageList.stream().forEach(pmsProductImage -> {
            pmsProductImage.setProductId(pmsProductInfo.getId());
            pmsProductImageMapper.insert(pmsProductImage);
        });
        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductInfo.getPmsProductSaleAttrList();
        pmsProductSaleAttrList.stream().forEach(pmsProductSaleAttr -> {
            pmsProductSaleAttr.setProductId(pmsProductInfo.getId());
            pmsProductSaleAttrMapper.insert(pmsProductSaleAttr);
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttr.getPmsProductSaleAttrValueList();
            pmsProductSaleAttrValueList.stream().forEach(pmsProductSaleAttrValue -> {
                pmsProductSaleAttrValue.setProductId(pmsProductInfo.getId());
                pmsProductSaleAttrValueMApper.insert(pmsProductSaleAttrValue);
            });
        });
        Jedis redis = redisUtil.getJedis();
        String skuKey = "spu:" + pmsProductInfo.getId() + ":info";
        redis.del(skuKey);
        redis.close();
    }

    @Override
    public List<PmsProductSaleAttr> selectSpuSaleAttrListCheckBySku(String productId, String skuId) {
        List<PmsProductSaleAttr> pmsProductSaleAttrs = new ArrayList<PmsProductSaleAttr>();
//        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
//        pmsProductSaleAttr.setProductId(productId);
//        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
//        for (PmsProductSaleAttr pmsProductSaleAttr1 :pmsProductSaleAttrList) {
//            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
//            pmsProductSaleAttrValue.setSaleAttrId(pmsProductSaleAttr1.getSaleAttrId());
//            pmsProductSaleAttrValue.setProductId(productId);
//            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttrValueMApper.select(pmsProductSaleAttrValue);
//            pmsProductSaleAttr1.setPmsProductSaleAttrValueList(pmsProductSaleAttrValueList);
//        }
        Jedis redis = redisUtil.getJedis();
        String skuKey = "spu:" + productId + ":info";
        String skuJson = redis.get(skuKey);
        if (skuJson != null) {
            pmsProductSaleAttrs = JSONArray.parseArray(skuJson, PmsProductSaleAttr.class);
        } else {
            pmsProductSaleAttrs = selectSpuSaleAttrListFromDb(productId, skuId);
            if (pmsProductSaleAttrs != null && pmsProductSaleAttrs.size() > 0) {
                redis.set(skuKey, JSON.toJSONString(pmsProductSaleAttrs));
            }
        }
        return pmsProductSaleAttrs;
    }


    public List<PmsProductSaleAttr> selectSpuSaleAttrListFromDb(String productId, String skuId) {
        return pmsProductSaleAttrMapper.selectSpuSaleAttrListCheckBySku(productId, skuId);
    }
}
