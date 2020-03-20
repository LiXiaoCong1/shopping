package com.atguigu.gmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.atguigu.gmall.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMember> getAllUser(){
//       return userMapper.selectAllUser();
       return userMapper.selectAll();
    }

    @Override
    public void deleteUserById(String id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateUser(UmsMember umsMember) {
        userMapper.updateByPrimaryKey(umsMember);
    }

    @Override
    public List<UmsMemberReceiveAddress> findAddressByMemberId(String id) {
        Example example = new Example(UmsMemberReceiveAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberId",id);
        return umsMemberReceiveAddressMapper.selectByExample(example);
    }

    @Override
    public void addUser(UmsMember umsMember) {
        userMapper.insert(umsMember);
    }

    @Override
    public void addUmsMemberReceiveAddress(UmsMemberReceiveAddress umsMemberReceiveAddress) {
        umsMemberReceiveAddress.setPhoneNumber("13476346876");
        umsMemberReceiveAddress.setMemberId("1");
        umsMemberReceiveAddressMapper.insert(umsMemberReceiveAddress);
    }
}
