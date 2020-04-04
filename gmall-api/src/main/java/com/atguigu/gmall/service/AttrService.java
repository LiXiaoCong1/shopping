package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;

import java.util.List;

/**
 * @author lxc
 * @date 2020/3/25 0:27
 */
public interface AttrService {
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    public void saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    public List<PmsBaseAttrValue> attrValueList(String attrInfoId);

    public void deleteAttrValueById(String id);

    public List<PmsBaseSaleAttr> baseSaleAttrList(); //销售属性名称
}
