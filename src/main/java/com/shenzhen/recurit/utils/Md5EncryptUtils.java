package com.shenzhen.recurit.utils;

import com.shenzhen.recurit.constant.OrdinaryConstant;
import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5EncryptUtils {

    // 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static void main(String[] args) {
        System.out.println(encryptMd5("malinbo"));
    }

    public static String encryptMd5(String str){
        if(EmptyUtils.isEmpty(str)){
            return OrdinaryConstant.IS_BLACK;
        }
        String hesString = OrdinaryConstant.IS_BLACK;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            str=str+"recurit";
            messageDigest.update(str.getBytes());
            byte[] digest = messageDigest.digest();
            hesString=byteToString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hesString;
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 返回形式只为数字
    private static String byteToNum(byte bByte) {
        int iRet = bByte;
       // System.out.println("iRet1=" + iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf(iRet);
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuilder sBuffer = new StringBuilder();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

}
