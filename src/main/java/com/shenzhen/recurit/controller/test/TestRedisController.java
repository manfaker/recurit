package com.shenzhen.recurit.controller.test;

import com.shenzhen.recurit.utils.RedisTempleUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
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

    public static void main(String[] args) throws MessagingException {
        // 创建Properties 类用于记录邮箱的一些属性
        Properties props = new Properties();
        // 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", "true");
        //此处填写SMTP服务器
        props.put("mail.smtp.host", "smtp.qq.com");
        //端口号，QQ邮箱端口587
        props.put("mail.smtp.port", "587");
        // 此处填写，写信人的账号
        props.put("mail.user", "1050484730@qq.com");
        // 此处填写16位STMP口令

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = "proqrvrpxmlfbfai";
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
        message.setFrom(form);

        // 设置收件人的邮箱
        InternetAddress to = new InternetAddress("1269926929@qq.com");
        message.setRecipient(Message.RecipientType.TO, to);

        // 设置邮件标题
        message.setSubject("测试邮件");

        // 设置邮件的内容体
        message.setContent("内容", "text/html;charset=UTF-8");

        // 最后当然就是发送邮件啦
        Transport.send(message);
        System.out.println("完成");
    }
}
