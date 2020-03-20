package com.atguigu.gmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;


@Controller
public class UserController {





    @Reference
    private UserService userService;



    @RequestMapping("getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser() {
        return userService.getAllUser();
    }

 
    @RequestMapping(value = "delectUserById/{id}")
    @ResponseBody
    public void delectUser(@PathVariable String id) {
        userService.deleteUserById(id);
    }

    @RequestMapping(value = "updateUser")
    @ResponseBody
    public void updateUser(@RequestBody UmsMember umsMember) {
        userService.updateUser(umsMember);
    }


    @RequestMapping(value = "findAddressByMemberId/{id}")
    @ResponseBody
    public List<UmsMemberReceiveAddress> findAddressByMemberId(@PathVariable String id) {
        return userService.findAddressByMemberId(id);
    }

    @RequestMapping(value = "addUmsMemberReceiveAddress")
    @ResponseBody
    public void addUmsMemberReceiveAddress(@RequestBody UmsMemberReceiveAddress UmsMemberReceiveAddress) {
//        String s1 = "sw"+12345;
//        String s2 = new String("sw");
//        String s3 = new String("sw1");
//        if(s1.equals(s2)){
//
//        }
//        if(s2.equals(s3)){
//
//        }


        userService.addUmsMemberReceiveAddress(UmsMemberReceiveAddress);
    }




}