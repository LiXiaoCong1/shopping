package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.service.AttrService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lxc
 * @date 2020/3/25 0:34
 */
@RestController
@CrossOrigin
public class AttrController {

    @Reference
    private AttrService attrService;

    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id)    {
        return attrService.attrInfoList(catalog3Id);
    }

    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public void saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        attrService.saveAttrInfo(pmsBaseAttrInfo);
    }

    @RequestMapping("getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList(String attrId){
        return attrService.attrValueList(attrId);
    }

    @RequestMapping("deleteAttrValueById")
    @ResponseBody
    public String deleteAttrValueById(String id){
        attrService.deleteAttrValueById(id);
        return "success";
    }

    @RequestMapping("baseSaleAttrList")
    public List<PmsBaseSaleAttr> baseSaleAttrList(){
       return attrService.baseSaleAttrList();
    }

}
