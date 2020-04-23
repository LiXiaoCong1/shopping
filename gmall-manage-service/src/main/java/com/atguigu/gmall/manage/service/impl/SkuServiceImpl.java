package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.bean.PmsSkuImage;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuImageMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lxc
 * @date 2020/4/5 22:29
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    private PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    private PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    private PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        List<PmsSkuAttrValue> pmsSkuAttrValueList = pmsSkuInfo.getPmsSkuAttrValueList();
        pmsSkuAttrValueList.stream().forEach(pmsSkuAttrValue -> {
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        });
        List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = pmsSkuInfo.getPmsSkuSaleAttrValueList();
        pmsSkuSaleAttrValueList.stream().forEach(pmsSkuSaleAttrValue -> {
            pmsSkuSaleAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        });
        List<PmsSkuImage> pmsSkuImageList = pmsSkuInfo.getPmsSkuImageList();
        pmsSkuImageList.stream().forEach(pmsSkuImage -> {
            pmsSkuImage.setSkuId(pmsSkuInfo.getId());
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        });
        Jedis jedis = redisUtil.getJedis();
        String skuKey = "sku:" + pmsSkuInfo.getProductId() + ":info";
        String skuKey1 = "sku:" + pmsSkuInfo.getId() + ":info";
        jedis.del(skuKey, skuKey1);
        jedis.close();

    }

    public PmsSkuInfo selectPmsSkuInfoFromDb(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        pmsSkuInfo1.setPmsSkuImageList(pmsSkuImages);
        return pmsSkuInfo1;
    }

    @Override
    public PmsSkuInfo selectPmsSkuInfoById(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
//        pmsSkuInfo.setId(skuId);
        Jedis redis = redisUtil.getJedis();
        String skuKey = "sku:" + skuId + ":info";
        String skuJson = redis.get(skuKey);
        if (StringUtils.isNotBlank(skuJson)) {
            pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
        } else {
            pmsSkuInfo = selectPmsSkuInfoFromDb(skuId);
            if (pmsSkuInfo != null) {
                redis.set(skuKey, JSON.toJSONString(pmsSkuInfo));
            }else{
                //防止缓存穿透，将null或者""给redis
                redis.setex(skuKey,60*3,JSON.toJSONString(""));
            }
        }
        redis.close();
        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> selectSkuSaleAttrValueListBySpu(String productId) {
        List<PmsSkuInfo> pmsSkuInfoList = new ArrayList<>();
        Jedis redis = redisUtil.getJedis();
        String skuKey = "sku:" + productId + ":info";
        String skuJson = redis.get(skuKey);
        if (StringUtils.isNotBlank(skuJson)) {
            pmsSkuInfoList = JSONArray.parseArray(skuJson, PmsSkuInfo.class);
        } else {
            pmsSkuInfoList = selectSkuSaleAttrValueListFromDb(productId);
            if (pmsSkuInfoList != null && pmsSkuInfoList.size() > 0) {
                redis.set(skuKey, JSON.toJSONString(pmsSkuInfoList));
            }
        }
        redis.close();
        return pmsSkuInfoList;
    }


    public List<PmsSkuInfo> selectSkuSaleAttrValueListFromDb(String productId) {
        return pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);
    }
}


