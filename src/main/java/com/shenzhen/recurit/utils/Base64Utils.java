package com.shenzhen.recurit.utils;


import com.shenzhen.recurit.constant.OrdinaryConstant;
import org.apache.commons.lang.StringUtils;

import java.util.Base64;

public class Base64Utils {

    public static void main(String[] args) {
        //测试加密解密
        String str = encryptBASE64("LTAI4FgEZ19NGXpfQU8ksggy");
        System.out.println("加密后：" + str);
        System.out.println("解密后：" +decryptBASE64(str));
        String str1 = encryptBASE64("vbC3xQDOhEcKo4vWD97jaE68hUXIqO");
        System.out.println("加密后：" + str1);
        System.out.println("解密后：" +decryptBASE64(str1));
        String str2 = encryptBASE64("SMS_183260274");
        System.out.println("加密后：" + str2);
        System.out.println("解密后：" +decryptBASE64(str2));
    }
    /**
     * 解密
     * @param str
     * @return
     */
    public static String decryptBASE64(String str){
        if(StringUtils.isEmpty(str)){
            return OrdinaryConstant.IS_BLACK;
        }
        byte[] decoded = Base64.getDecoder().decode(str);
        String decodeStr = new String(decoded);
        return decodeStr;
    }

    /**
     * BASE64加密
     */
    public static String encryptBASE64(String str){
        if(StringUtils.isEmpty(str)){
            return OrdinaryConstant.IS_BLACK;
        }
        byte[] bytes = str.getBytes();
        String encoded = Base64.getEncoder().encodeToString(bytes);
        return encoded;
    }
}
