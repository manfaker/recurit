package com.shenzhen.recurit.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:${variable.dev.path}")
@Data
public class VaribaleUtils {
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
}
