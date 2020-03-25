package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsBaseAttrInfo;

import java.util.List;

/**
 * @author lxc
 * @date 2020/3/25 0:27
 */
public interface AttrInfoService {
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    public void saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);


}
