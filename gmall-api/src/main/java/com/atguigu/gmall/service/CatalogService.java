package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsBaseCatalog1;
import com.atguigu.gmall.bean.PmsBaseCatalog2;
import com.atguigu.gmall.bean.PmsBaseCatalog3;

import java.util.List;

/**
 * @author lxc
 * @date 2020/3/21 16:58
 */
public interface CatalogService {
    List<PmsBaseCatalog1> catalog1List();

    List<PmsBaseCatalog2> catalog2List(String catalog1Id);

    List<PmsBaseCatalog3> catalog3List(String catalog2Id);
}
