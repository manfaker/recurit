package com.shenzhen.recurit.constant;

public interface InformationConstant {

    /**
     * 手机验证部分配置
     */
    // 设置超时时间-可自行调整
    String  DEFAULT_CONNECT_TIME_OUT  = "sun.net.client.defaultConnectTimeout";
    String  DEFAULT_READ_TIME_OUT = "sun.net.client.defaultReadTimeout";
    String  TIME_OUT = "10000";
    // 初始化ascClient需要的几个参数
    String PRODUCT = "Dysmsapi";   // 短信API产品名称（短信产品名固定，无需修改）
    String DOMAIN = "dysmsapi.aliyuncs.com";// 短信API产品域名（接口地址固定，无需修改）
    //
    String ACCESS_KEY_ID = "TFRBSTRGZ0VaMTlOR1hwZlFVOGtzZ2d5";// accessKeyId
    String ACCESS_KEY_SECRET = "dmJDM3hRRE9oRWNLbzR2V0Q5N2phRTY4aFVYSXFP";//accessKeySecret
    // 必填:短信签名-可在短信控制台中找到
    String SIGN_NAME = "腾雾";// 短信签名
    // 必填:短信模板-可在短信控制台中找到
    String TEMPLATE_CODE = "U01TXzE4MzI2MDI3NA=="; // 短信模板

    //邮箱验证部分配置
    String SPRING_MAIL_HOST = "smtp.qq.com";
    String spring_mail_username = "MTA1MDQ4NDczMEBxcS5jb20=";
    String spring_mail_password = "cHJvcXJ2cnB4bWxmYmZhaQ==";//不是密码，是授权码
    String spring_mail_port = "587";
    String spring_mail_smtp_auth = "true";
    String NUMBER = "number";
    String CODE = "code";
    String ROLE_NUM = "roleNum";

    //header 请求头
    String AUTH_USER = "auth-user";

    //缓存同步时间
    long INITIAL_TIME=946691076849l;
    String KEY_DICTIONARY_TIME = "dictionary_time";

    //角色信息
    String RECURIT ="recurit";
    String EMAIL = "email";
    String PHONE = "phone";
    String USERCODE = "userCode";
    String USERNAME= "userName";
    String PASSWORD = "password";
    String NEW_PASSWORD = "newPassword";
    String ADMIN = "admin";           //超级管理员
    String ENTERPRISE = "enterprise"; //企业用户
    String JOBSEEKER = "jobseeker";   //普通用户

    //请求头，获取IP地址
    String X_FORWARDED_FOR ="x-forwarded-for";
    String UNKNOWN = "unknown";
    String PROXY_CLIENT_IP="Proxy-Client-IP";
    String WL_PROXY_CLIENT_IP="WL-Proxy-Client-IP";




}
