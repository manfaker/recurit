package com.shenzhen.recurit.controller.test;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.enums.ReturnEnum;
import com.shenzhen.recurit.pojo.ResultPojo;
import com.shenzhen.recurit.service.test.TestUserService;
import com.shenzhen.recurit.utils.ApplyConfigUtils;
import com.shenzhen.recurit.utils.EmptyUtils;
import com.shenzhen.recurit.utils.RedisTempleUtils;
import com.shenzhen.recurit.utils.VaribaleUtils;
import com.shenzhen.recurit.vo.test.TestUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "test/user")
@Api(tags = {"测试"})
public class TestUserController {

    @Autowired
    private TestUserService userService;
    @Resource
    private RedisTempleUtils redisTempleUtils;


    @RequestMapping(value = "/getUserByName",method = RequestMethod.GET)
    @ResponseBody
    public String getUserByName(String name){
       // String name = "晓霜";
        TestUserVO testUserVO = userService.getUserByName(name);
        return testUserVO.getDemo();
    }

    @PostMapping(value = "/getOrSetTimeInterval")
    @ApiImplicitParam(value = "时间间隔",name="timeInterVal",required = false)
    @ResponseBody
    public ResultPojo getOrSetTimeInterval(Integer timeInterVal){
        if(EmptyUtils.isNotEmpty(timeInterVal)){
            redisTempleUtils.setValue("timeInterVal",timeInterVal);
            return ResultPojo.success(ReturnEnum.SUCCESS.getValue(),timeInterVal);
        }
        Integer interVal = redisTempleUtils.getValue("timeInterVal",Integer.class);
        if(EmptyUtils.isEmpty(interVal)||interVal==2){
            interVal = NumberEnum.TWO.getValue();
            redisTempleUtils.setValue("timeInterVal",timeInterVal);
        }
        return ResultPojo.success(ReturnEnum.SUCCESS.getValue(),interVal);
    }

}
