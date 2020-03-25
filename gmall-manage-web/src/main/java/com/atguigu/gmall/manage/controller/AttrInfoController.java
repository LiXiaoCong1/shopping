package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.service.AttrInfoService;
import com.atguigu.gmall.service.AttrValueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author lxc
 * @date 2020/3/25 0:34
 */
@Controller
@CrossOrigin
public class AttrInfoController {

    @Reference
    private AttrInfoService attrInfoService;

    @Reference
    private AttrValueService attrValueService;

    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id)    {
        return attrInfoService.attrInfoList(catalog3Id);
    }

    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public void saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        attrInfoService.saveAttrInfo(pmsBaseAttrInfo);
    }
}
