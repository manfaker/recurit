package com.shenzhen.recurit.controller.test;

import com.shenzhen.recurit.utils.RedisTempleUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;

@Controller
@RequestMapping(value = "test/redis")
public class TestRedisController {

    @Resource
    private RedisTempleUtils redisTempleUtils;

    @ResponseBody
    @RequestMapping(value = "getUserName")
    public String getUserName(){
        boolean flag = redisTempleUtils.setValue("xiaoshuang","一个漂亮的大姑娘");
        System.out.println(flag);
        return redisTempleUtils.getValue("xiaoshuang",String.class).toString();
    }
}
