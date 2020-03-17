package com.shenzhen.recurit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.enums.ReturnEnum;
import com.shenzhen.recurit.service.UserService;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.EncryptBase64Utils;
import com.shenzhen.recurit.vo.ResultVO;
import com.shenzhen.recurit.vo.UserVO;
import org.apache.catalina.User;
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
        return ResultVO.error(ReturnEnum.DEFAULT_301.getCode(),"跳转到登录页面");
    }

    @RequestMapping(value = "noPrivilege",method = RequestMethod.GET)
    public Object noPrivilege(){
        return ResultVO.error(ReturnEnum.DEFAULT_401.getCode(),"无权限页面");
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

    @RequestMapping(value = "addUser",method = RequestMethod.POST)
    public Object addUser(@RequestBody String jsonData){
        UserVO userVO = JSONObject.parseObject(jsonData, UserVO.class);
        return userService.addUser(userVO);
    }

    @RequestMapping(value = "updatePassword",method = RequestMethod.POST)
    public Object updatePassword(@RequestBody String jsonData){
        return userService.updatePassword(jsonData);
    }




    @RequestMapping(value = "loginUser",method = RequestMethod.POST)
    public Object loginUser(@RequestBody String jsonData){
        return userService.loginUser(jsonData);
    }

    @RequestMapping(value = "exitUser",method = RequestMethod.POST)
    public Object exitUser(@RequestBody String jsonData){
        return userService.logoutUser(jsonData);
    }

    @RequestMapping(value = "getUserInfo",method = RequestMethod.GET)
    public Object getUserInfoCookie(String  userCode){
        return ResultVO.success(userService.getUserInfoCookie(userCode));
    }

    @RequestMapping(value = "getUserInfoByNameOrNumber",method = RequestMethod.POST)
    public Object getUserInfoByNameOrNumber(@RequestBody String  jsonData){
        UserVO user = userService.getUserInfoByNameOrNumber(jsonData);
        if(EmptyUtils.isNotEmpty(user)){
            return ResultVO.error("用户已存在");
        }else{
            return ResultVO.success("验证通过");
        }
    }

    @RequestMapping(value = "updateUser",method = RequestMethod.PUT)
    public Object updateUser(@RequestBody String jsonData){
        UserVO user = JSONObject.parseObject(jsonData,UserVO.class);
        UserVO currUser=null;
        if(EmptyUtils.isNotEmpty(user.getUserName())){
            currUser = userService.getUserByName(user.getUserName());
            if(EmptyUtils.isNotEmpty(currUser)&&user.getId()!=currUser.getId()){
                return ResultVO.error("用户名已存在，请重新填写");
            }
        }
        if(EmptyUtils.isNotEmpty(user.getPhone())){
            currUser = userService.getUserByPhone(user.getPhone());
            if(EmptyUtils.isNotEmpty(currUser)&&user.getId()!=currUser.getId()){
                return ResultVO.error("手机号已存在，请重新填写");
            }
        }
        if(EmptyUtils.isNotEmpty(user.getEmail())){
            currUser = userService.getUserByEmail(user.getEmail());
            if(EmptyUtils.isNotEmpty(currUser)&&user.getId()!=currUser.getId()){
                return ResultVO.error("邮箱已存在，请重新填写");
            }
        }
        UserVO  userVO = userService.updateUser(user);
        return ResultVO.success(userVO);
    }
}
