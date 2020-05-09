package com.shenzhen.recurit.utils;

import com.shenzhen.recurit.constant.InformationConstant;
import com.shenzhen.recurit.enums.ReturnEnum;
import com.shenzhen.recurit.pojo.UserPojo;
import com.shenzhen.recurit.utils.word.WordUtil;
import com.shenzhen.recurit.vo.ResultVO;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class EmailUtils {

    private static VaribaleUtils varibaleUtils=null;

    private static void init(){
        if(EmptyUtils.isEmpty(varibaleUtils)){
            varibaleUtils=SpringUtils.getBean(VaribaleUtils.class);
        }
    }


    public static ResultVO sendEmail(String accept){
        String code = PhoneUtils.getCode();
        MimeMessage message=getMessage();
        final String userName = EncryptBase64Utils.decryptBASE64(varibaleUtils.getMailName());
        setCodeMessage(message,userName,accept,code);
        return ResultVO.success(ReturnEnum.SUCCESS.getValue(),code);
    }

    public static MimeMessage getMessage(){
        // 创建Properties 类用于记录邮箱的一些属性
        init();
        Properties props = new Properties();
        final String userName = EncryptBase64Utils.decryptBASE64(varibaleUtils.getMailName());
        final String password = EncryptBase64Utils.decryptBASE64(varibaleUtils.getMailPassword());
        // 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", varibaleUtils.getMailAuth());
        //此处填写SMTP服务器
        props.put("mail.smtp.host", varibaleUtils.getMailHost());
        //端口号，QQ邮箱端口587
        props.put("mail.smtp.port", varibaleUtils.getMailPort());
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
        return message;
    }

    private static void setCodeMessage(MimeMessage message,String userName, String accept,String code){
        InternetAddress form;
        InternetAddress to;
        try {
            form = new InternetAddress(userName);
            // 设置收件人的邮箱
            to = new InternetAddress(accept);
            message.setFrom(form);
            message.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件标题
            message.setSubject("上海仁格人力资源有限公司");
            // 设置邮件的内容体
            message.setContent("感谢您使用上海仁格人力资源招聘网站: \t\n 您的验证码是"+code+",请于5分钟类完成注册否则会失效！", "text/html;charset=UTF-8");
            Transport.send(message);
        }catch (MessagingException messagingException){
            messagingException.printStackTrace();
        }
    }

    private static void setResumeMessage(MimeMessage message,String userName, String accept,UserPojo userPojo){
        InternetAddress form;
        InternetAddress to;
        File resumeFiel = null;
        try {
            String docName = InformationConstant.TEMPLATE+System.currentTimeMillis()+InformationConstant.RESUME_DOCX;
            String docPath = WordUtil.path+ File.separator+InformationConstant.TEMPLATE +File.separator+docName;
            WordUtil.createResume(docPath,userPojo);
            resumeFiel = new File(docPath);
            form = new InternetAddress(userName);
            // 设置收件人的邮箱
            to = new InternetAddress(accept);
            message.setFrom(form);
            message.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件标题
            message.setSubject("简历信息");
            // 设置邮件的内容体
           // message.setContent("请查收附件", "text/html;charset=UTF-8");
            // 创建消息部分
            BodyPart messageBodyPart = new MimeBodyPart();
            // 消息
            messageBodyPart.setText("请查收附件");
            // 创建多重消息
            Multipart multipart = new MimeMultipart();
            // 设置文本消息部分
            multipart.addBodyPart(messageBodyPart);
            // 附件部分
            messageBodyPart = new MimeBodyPart();
            // 设置要发送附件的文件路径
            DataSource source = new FileDataSource(resumeFiel);
            messageBodyPart.setDataHandler(new DataHandler(source));

            // messageBodyPart.setFileName(filename);
            // 处理附件名称中文（附带文件路径）乱码问题
            messageBodyPart.setFileName(MimeUtility.encodeText(resumeFiel.getName()));
            multipart.addBodyPart(messageBodyPart);
            // 发送完整消息
            message.setContent(multipart);
            Transport.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            resumeFiel.delete();
        }
    }

    public static void sendResume(String email , UserPojo userPojo){
        MimeMessage message=getMessage();
        final String userName = EncryptBase64Utils.decryptBASE64(varibaleUtils.getMailName());
        setResumeMessage(message,userName,email,userPojo);
    }

}
