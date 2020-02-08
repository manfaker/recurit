package com.shenzhen.recurit.controller.test;

import com.shenzhen.recurit.enums.ReqturnEnum;
import com.shenzhen.recurit.utils.EmailUtils;
import com.shenzhen.recurit.utils.RedisTempleUtils;
import com.shenzhen.recurit.vo.ResultVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/test/email")
public class EmailTestController {

    @Resource
    private RedisTempleUtils redisTempleUtils;

    //读取配置文件邮箱账号参数

    //发送待验证码的邮件
    @RequestMapping(value = "/sendEmail",method = RequestMethod.GET)
    @ResponseBody
    public String sendEmail() {
        String accepter = "1269926929@qq.com";
        ResultVO resultVO = EmailUtils.sendEmail(accepter);
        if(ReqturnEnum.SUCCESS.getCode()==resultVO.getCode()){
            redisTempleUtils.setValue(accepter,resultVO.getData().toString(),30000, TimeUnit.SECONDS);
        }
        return "邮件发送成功";
    }


    //验证邮箱验证码是否正确


}
