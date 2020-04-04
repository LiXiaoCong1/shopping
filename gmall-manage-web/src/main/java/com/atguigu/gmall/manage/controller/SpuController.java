package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.service.SpuService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import util.PmsUploadUtil;

import java.util.List;

//import util.PmsUploadUtil;

/**
 * @author lxc
 * @date 2020/3/28 21:04
 */

@RestController
@CrossOrigin
public class SpuController {

    @Reference
    private SpuService spuService;

    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(String catalog3Id ){
        return spuService.spuList(catalog3Id);
    }

    @RequestMapping("spuSaleAttrList")
    public List<PmsProductSaleAttr> spuSaleAttrList( String spuId){
        return spuService.spuSaleAttrList(spuId);
    }

    @RequestMapping("spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(String spuId) {
      return  spuService.spuImageList(spuId);
    }

    @RequestMapping("saveSpuInfo")
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    /**
     * 上传图片/视频
     * @param multipartFile
     * @return
     * @throws Exception
     */
    @RequestMapping("fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile ) throws Exception {
        String image = PmsUploadUtil.uploadImage(multipartFile);

        return image;
    }

}
