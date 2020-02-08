package com.shenzhen.recurit.utils;

import org.apache.commons.lang.StringUtils;

public class EmptyUtils {

    /**
     * 判断是否是空字符串
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return StringUtils.isEmpty(str);
    }
    /**
     * 判断是否是空对象
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj){
        if(obj == null){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断是否不是空字符串
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        return StringUtils.isNotEmpty(str);
    }

    /**
     * 判断是否不是空对象
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj){
        if(obj == null){
            return false;
        }else{
            return true;
        }
    }
}
