package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;




public interface UserService {

    List<UmsMember> getAllUser();

    void addUser(UmsMember umsMember);

    void deleteUserById(String id);

    void updateUser(UmsMember umsMember);

    List<UmsMemberReceiveAddress> findAddressByMemberId(String id);

    void addUmsMemberReceiveAddress(UmsMemberReceiveAddress umsMemberReceiveAddress);
}
