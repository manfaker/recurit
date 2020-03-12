package com.shenzhen.recurit.controller;

import com.alibaba.fastjson.JSON;
import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.enums.ReturnEnum;
import com.shenzhen.recurit.service.UserService;
import com.shenzhen.recurit.utils.EncryptBase64Utils;
import com.shenzhen.recurit.vo.ResultVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "reLogin",method = RequestMethod.GET)
    public Object reLogin(){
        return ResultVO.error(ReturnEnum.DEFAULT_302.getCode(),"跳转到登录页面");
    }

    @RequestMapping(value = "getVerificationCode",method = RequestMethod.GET)
    public Object getVerificationCode(String number){
        Object verificationCode = userService.getVerificationCode(number);
        return verificationCode;
    }

    @RequestMapping(value = "addByNumber",method = RequestMethod.POST)
    public Object addByNumber(@RequestBody String jsonData){
        return userService.addByNumber(jsonData);
    }

    @RequestMapping(value = "loginUser",method = RequestMethod.POST)
    public Object loginUser(@RequestBody String jsonData){
        return userService.loginUser(jsonData);
    }
    @RequestMapping(value = "exitUser",method = RequestMethod.POST)
    public Object exitUser(@RequestBody String jsonData){
        return userService.logoutUser(jsonData);
    }
}
