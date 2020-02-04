package com.shenzhen.recurit.controller.test;

import com.shenzhen.recurit.service.test.TestUserService;
import com.shenzhen.recurit.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "test/user")
public class TestUserController {

    @Autowired
    private TestUserService userService;


    @RequestMapping(value = "/getUserByName",method = RequestMethod.GET)
    @ResponseBody
    public String getUserByName(String name){
       // String name = "晓霜";
        UserVo userVo = userService.getUserByName(name);
        return userVo.getDemo();
    }
}
