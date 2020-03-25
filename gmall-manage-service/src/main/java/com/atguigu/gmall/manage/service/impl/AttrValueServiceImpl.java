package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.atguigu.gmall.service.AttrValueService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author lxc
 * @date 2020/3/25 0:47
 */
@Service
public class AttrValueServiceImpl implements AttrValueService {

    @Autowired
    private PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Override
    public List<PmsBaseAttrValue> attrInfoList(String attrInfoId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrInfoId);
        return pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
    }
}
