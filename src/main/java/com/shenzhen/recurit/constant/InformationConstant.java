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
    String SPRING_MAIL_USERNAME = "MTA1MDQ4NDczMEBxcS5jb20=";
    String SPRING_MAIL_PASSWORD = "cHJvcXJ2cnB4bWxmYmZhaQ==";//不是密码，是授权码
    //pop3 dxeufokhgqycbjeg
    //imap kvfypmvpurwxcagf
    String SPRING_MAIL_PORT = "587";
    String SPRING_MAIL_SMTP_AUTH = "true";
    String NUMBER = "number";
    String CODE = "code";
    String ROLE_NUM = "roleNum";
    String IMPORT = "import";
    String ROLE = "ROLE";
    String SEX = "SEX";
    String EXPORT = "export";

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

    //
    String MALE = "男";
    String FEMALE = "女";
    String ADMIN_EN = "管理员";           //超级管理员
    String ENTERPRISE_EN = "招聘者"; //企业用户
    String JOBSEEKER_EN = "求职者";   //普通用户


    //请求头，获取IP地址
    String X_FORWARDED_FOR ="x-forwarded-for";
    String UNKNOWN = "unknown";
    String PROXY_CLIENT_IP="Proxy-Client-IP";
    String WL_PROXY_CLIENT_IP="WL-Proxy-Client-IP";

    //
    String USER ="user";
    String UPPER_USER = "USER";
    String COMPANY = "company";
    String JOB_EXPERIENCE = "job_experience";
    String POSITION="position";

    //字典类别
    String SALARY = "SALARY";        //薪资
    String EXPERIENCE = "EXPERIENCE"; //经历
    String EDUCATION = "EDUCATION";   //教育
    String FINANCING = "FINANCING";   //融资
    String SCALE = "SCALE";   //公司规模

    //模板
    String TEMPLATE = "template";
    String RESUME_DOCX = "resume.docx"; //简历docx
    String RESUME_FTL = "resume.ftl";   //简历模板

    //字符级别
    String DATA_TYPE = "json";
    String UTF_8 = "UTF-8";

    //阿里和腾讯
    String ALIPAY ="ALIPAY";
    String WECHAT ="WECHAT";

    //simpleDateFormat
    String SIMPLE_DATE_FORMAT ="yyyy/MM/dd";



}
