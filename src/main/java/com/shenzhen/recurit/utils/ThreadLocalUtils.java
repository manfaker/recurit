package com.shenzhen.recurit.utils;

import com.shenzhen.recurit.vo.UserVO;

public class ThreadLocalUtils {
   private static  ThreadLocal<String> stringThreadLocal;

   private static RedisTempleUtils redisTempleUtils;

   public static void init(){
       if(EmptyUtils.isEmpty(stringThreadLocal)){
           stringThreadLocal = new ThreadLocal<>();
       }
   }

    /**
     * 获取用户动态口令
     * @return
     */
   public static String getUserCode(){
        return stringThreadLocal.get();
   }
    /**
     * 设置用户动态口令
     */
   public static void setUserCode(String userName){
       init();
       stringThreadLocal.set(userName);
   }

    /**
     * 获取登录用户的信息
     * @return
     */
   public static UserVO getUser(){
       redisTempleUtils = SpringUtils.getBean(RedisTempleUtils.class);
       return redisTempleUtils.getValue(ThreadLocalUtils.getUserCode(),UserVO.class);
   }
}
