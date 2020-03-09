package com.shenzhen.recurit.utils;

import com.shenzhen.recurit.vo.UserVO;

public class ThreadLocalUtils {
   private static  ThreadLocal<String> stringThreadLocal;

   public static void init(){
       if(EmptyUtils.isEmpty(stringThreadLocal)){
           stringThreadLocal = new ThreadLocal<>();
       }
   }

    /**
     * 获取用户动态口令
     * @return
     */
   public static String getUser(){
        return stringThreadLocal.get();
   }
    /**
     * 设置用户动态口令
     */
   public static void setUserCode(String userName){
       init();
       String encodeUser = EncryptBase64Utils.encryptBASE64(userName);
       stringThreadLocal.set(encodeUser);
   }
}
