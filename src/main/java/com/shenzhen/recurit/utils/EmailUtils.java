package com.shenzhen.recurit.utils;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.enums.ReqturnEnum;
import com.shenzhen.recurit.vo.ResultVO;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtils {

    public static ResultVO sendEmail(String accept){
        // 创建Properties 类用于记录邮箱的一些属性
        Properties props = new Properties();
        final String userName = Base64Utils.decryptBASE64(InformationConstant.spring_mail_username);
        final String password = Base64Utils.decryptBASE64(InformationConstant.spring_mail_password);
        // 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", InformationConstant.spring_mail_smtp_auth);
        //此处填写SMTP服务器
        props.put("mail.smtp.host", InformationConstant.SPRING_MAIL_HOST);
        //端口号，QQ邮箱端口587
        props.put("mail.smtp.port", InformationConstant.spring_mail_port);
        // 此处填写，写信人的账号
        props.put("mail.user",userName);

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress form;
        InternetAddress to;
        String code = PhoneUtils.getCode();
        try {
            form = new InternetAddress(userName);
            // 设置收件人的邮箱
            to = new InternetAddress(accept);
            message.setFrom(form);
            message.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件标题
            message.setSubject("测试邮件");
            // 设置邮件的内容体
            message.setContent("尊敬的用户您好：\t\n 您的验证码是"+code+",请于5分钟类完成注册否则会失效！", "text/html;charset=UTF-8");
            // 最后当然就是发送邮件啦
            Transport.send(message);
        }catch (MessagingException messagingException){
            messagingException.printStackTrace();
        }
        return ResultVO.success(ReqturnEnum.SUCCESS.getValue(),code);
    }
}
