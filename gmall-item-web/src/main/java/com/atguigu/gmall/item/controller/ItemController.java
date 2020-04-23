package com.atguigu.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author lxc
 * @date 2020/4/10 16:01
 */
@Controller
@CrossOrigin
public class ItemController {

    @Reference
    private SkuService skuService;

    @Reference
    private SpuService spuService;

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId,ModelMap modelMap){
        PmsSkuInfo pmsSkuInfo = skuService.selectPmsSkuInfoById(skuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrList = spuService.selectSpuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),pmsSkuInfo.getId());
        modelMap.put("skuInfo",pmsSkuInfo);
        modelMap.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrList);
        List<PmsSkuInfo> pmsSkuInfos = skuService.selectSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());
        HashMap<String, String> map = new HashMap<>();
        pmsSkuInfos.stream().forEach(skuInfo -> {
            String k = "";
            String v = skuInfo.getId();
            List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = skuInfo.getPmsSkuSaleAttrValueList();
            for(PmsSkuSaleAttrValue pmsSkuSaleAttrValue:pmsSkuSaleAttrValueList){
                k += pmsSkuSaleAttrValue.getSaleAttrValueId()+"|";
            }
            map.put(k,v);
        });
        String s = JSON.toJSONString(map);
        modelMap.put("valuesSku",s);
        System.out.println(modelMap);
        return "item";
    }

    @RequestMapping("index")
    public String index(ModelMap modelMap){
        modelMap.put("hello","hello thymeleaf!!!");
        ArrayList<String> list = new ArrayList<>();

        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();
        ArrayList<String> list4 = new ArrayList<>();
        for (int i=0;i<5;i++){
            list.add("循环数据"+i);
        }
        modelMap.put("list",list);
        return "index";
    }





}
