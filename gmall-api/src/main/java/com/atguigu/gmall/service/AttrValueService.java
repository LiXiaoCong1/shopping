package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsBaseAttrValue;

import java.util.List;

/**
 * @author lxc
 * @date 2020/3/25 0:45
 */
public interface AttrValueService {
    public List<PmsBaseAttrValue> attrInfoList(String attrInfoId);
}
