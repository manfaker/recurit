package com.shenzhen.recurit.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:${variable.dev.path}")
@Data
public class VaribaleUtils {
    //支付宝
    @Value("${apply.config.appId}")
    private String appId;
    @Value("${merchant.private.key}")
    private String privateKey;
    @Value("${alipy.public.key}")
    private String publicKey;
    @Value("${notify.url}")
    private String notifyUrl;
    @Value("${return.url}")
    private String returnUrl;
    @Value("${sign.type}")
    private String signType;
    @Value("${gete.way.url}")
    private String geteWayUrl;
    //邮箱
    @Value("${spring_mail_username}")
    private String mailName;
    @Value("${spring_mail_password}")
    private String mailPassword;
    @Value("${spring_mail_port}")
    private String mailPort;
    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring_mail_smtp_auth}")
    private String mailAuth;

    @Value("${document.url}")
    private String documentUrl;
}
