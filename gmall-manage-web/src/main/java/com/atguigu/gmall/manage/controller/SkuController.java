package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lxc
 * @date 2020/4/5 22:26
 */

@RestController
@CrossOrigin
public class SkuController {

    @Reference
    public SkuService skuService;

    @RequestMapping("saveSkuInfo")
    public String saveSkuInfo (@RequestBody PmsSkuInfo pmsSkuInfo){
        pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());
        skuService.saveSkuInfo(pmsSkuInfo);
        if(StringUtils.isBlank(pmsSkuInfo.getSkuDefaultImg())){
            pmsSkuInfo.setSkuDefaultImg(pmsSkuInfo.getPmsSkuImageList().get(0).getImgUrl());
        }
        return "success";
    }
}
