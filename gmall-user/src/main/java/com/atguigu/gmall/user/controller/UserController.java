package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;




@Controller
public class UserController {

    @Autowired
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
        userService.addUmsMemberReceiveAddress(UmsMemberReceiveAddress);
    }


}