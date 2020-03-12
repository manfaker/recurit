package com.shenzhen.recurit.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTempleUtils {

    private static Logger logger = LoggerFactory.getLogger(RedisTempleUtils.class);
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 设置缓存
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean setValue(String key ,T value){
        try {
            //任意类型转换成String
            String val = beanToString(value);
            if(val==null||val.length()<=0){
                return false;
            }
            stringRedisTemplate.opsForValue().set(key,val);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过key获取对象
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getValue(String key,Class<T> clazz){
        try {
            String value = stringRedisTemplate.opsForValue().get(key);

            return stringToBean(value,clazz);
        }catch (Exception e){
            return null ;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T stringToBean(String value, Class<T> clazz) {
        if(value==null||value.length()<=0||clazz==null){
            return null;
        }

        if(clazz ==int.class ||clazz==Integer.class){
            return (T)Integer.valueOf(value);
        }
        else if(clazz==long.class||clazz==Long.class){
            return (T)Long.valueOf(value);
        }
        else if(clazz==String.class){
            return (T)value;
        }else {
            return JSON.toJavaObject(JSON.parseObject(value),clazz);
        }
    }

    /**
     *
     * @param  value 任意类型
     * @return String
     */
    private <T> String beanToString(T value) {

        if(value==null){
            return null;
        }
        Class <?> clazz = value.getClass();
        if(clazz==int.class||clazz==Integer.class){
            return ""+value;
        }
        else if(clazz==long.class||clazz==Long.class){
            return ""+value;
        }
        else if(clazz==String.class){
            return (String)value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * 放入缓存同时设置过期时间
     * @param key    键
     * @param value  值
     * @param var3   时间
     * @param var5   时间单位
     * @param <T>
     * @return
     */
    public <T> boolean setValue(String key ,T value,long var3, TimeUnit var5){
        try {
            //任意类型转换成String
            String val = beanToString(value);
            if(val==null||val.length()<=0){
                return false;
            }
            stringRedisTemplate.opsForValue().set(key,val,var3,var5);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    /**
     * 删除缓存
     * @param key    键
     */
    public boolean deleteValue(String key){
        try{
            stringRedisTemplate.delete(key);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
