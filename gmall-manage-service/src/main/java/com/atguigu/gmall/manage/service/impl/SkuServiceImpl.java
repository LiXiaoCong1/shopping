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
import java.util.UUID;

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
            System.out.println("取缓存中数据！");
            pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
        } else {
            //申请拿锁
            String token = UUID.randomUUID().toString();
            String ok = redis.set("sku:" + skuId + ":lock", token, "nx", "px", 200);
            System.out.println(ok);
            if (StringUtils.isNotBlank(ok) && "OK".equals(ok)) {
                System.out.println("取锁成功，开始查询数据库");
                //分布式缓存取锁
                //取锁成功，访问数据库
                pmsSkuInfo = selectPmsSkuInfoFromDb(skuId);
//                try {
//                    Thread.sleep(30 * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                if (pmsSkuInfo != null) {
                    redis.set(skuKey, JSON.toJSONString(pmsSkuInfo));
                } else {
                    //防止缓存穿透，将null或者""给redis
                    redis.setex(skuKey, 60 * 3, JSON.toJSONString(""));
                }
                //还锁
                System.out.println("还锁。。。");
                String lockToken = redis.get("sku:" + skuId + ":lock");
//                String script ="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//                redis.eval(script, Collections.singletonList("sku:" + skuId + ":lock"),Collections.singletonList(lockToken));
                if(StringUtils.isNotBlank(lockToken)&&lockToken.equals(token)){
                    redis.del("sku:" + skuId + ":lock");
                }
                System.out.println(lockToken);
            } else {
                System.out.println("线程睡眠3秒钟，启动自旋。。。");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("开始自旋");
                //取锁失败，开始自旋
                return selectPmsSkuInfoById(skuId);
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


