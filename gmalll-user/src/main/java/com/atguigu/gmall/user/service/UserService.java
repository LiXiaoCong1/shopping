package com.atguigu.gmall.user.service;

import com.atguigu.gmall.user.bean.UmsMember;
import com.atguigu.gmall.user.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {

    List<UmsMember> getAllUser();

    void addUser(UmsMember umsMember);

    void deleteUserById(String  id);

    void updateUser(UmsMember umsMember);

    List<UmsMemberReceiveAddress> findAddressByMemberId(String id);

    void addUmsMemberReceiveAddress(UmsMemberReceiveAddress umsMemberReceiveAddress);
}
