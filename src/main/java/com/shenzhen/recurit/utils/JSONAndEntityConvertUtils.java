package com.shenzhen.recurit.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.enums.NumberEnum;
import com.shenzhen.recurit.vo.OrderInfoVO;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONAndEntityConvertUtils<T> {

    /**
     * 兼容金额
     * @param t
     * @param <T>
     * @param flag true不转 int long * 0.01
     * @return
     */
    public static<T> JSONObject entityToIndexJSONObject(T t,boolean flag){
        JSONObject jsonObject = new JSONObject();
        if(EmptyUtils.isEmpty(t)){
            return jsonObject;
        }
        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if(EmptyUtils.isNotEmpty(fields)&&fields.length> NumberEnum.ZERO.getValue()){
            for(Field field :fields){
                field.setAccessible(true);
                String name = field.getName();
                name=capitalToIndex(name);
                Class<?> type = field.getType();
                try {
                    Object value = field.get(t);
                    if(EmptyUtils.isNotEmpty(value)){
                        if(int.class==type||Integer.class==type){
                            int digital = (int)value;
                            if(digital>NumberEnum.ZERO.getValue()){
                                if(flag){
                                    jsonObject.put(name,digital*0.01);
                                }else{
                                    jsonObject.put(name,digital);
                                }
                            }
                        }else if(double.class==type || Double.class==type){
                            double digital = (double)value;
                            if((double)value>NumberEnum.ZERO.getValue()){
                                if(flag){
                                    jsonObject.put(name,digital*0.01);
                                }else{
                                    jsonObject.put(name,digital);
                                }
                            }
                        }else if(long.class==type || Long.class==type){
                            long digital = (long)value;
                            if((long)value>NumberEnum.ZERO.getValue()){
                                if(flag){
                                    jsonObject.put(name,digital*0.01);
                                }else{
                                    jsonObject.put(name,digital);
                                }
                            }
                        }else if(Date.class==type){
                            jsonObject.put(name,(Date)value);
                        }else{
                            jsonObject.put(name,String.valueOf(value));
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObject;
    }

    private static String capitalToIndex(String value){
        if(EmptyUtils.isEmpty(value)){
            return OrdinaryConstant.IS_BLACK;
        }
        char[] chars = value.toCharArray();
        String regex = "^[A-Z]$";
        Pattern pattern=Pattern.compile(regex);
        StringBuilder stringBuilder = new StringBuilder(OrdinaryConstant.IS_BLACK);
        for(char ch:chars){
            String apha = String.valueOf(ch);
            Matcher matcher = pattern.matcher(apha);
            if(matcher.matches()){
                stringBuilder.append(OrdinaryConstant.SYMBOL_3).append(apha);
            }else{
                stringBuilder.append(apha);
            }
        }
        return stringBuilder.toString();
    }


    public static void main(String[] args) {
        OrderInfoVO order = new OrderInfoVO();
        int num = 234;
        order.setBody("sssssss");
        order.setBizType("fdasdfasdf");
        order.setDiscount(123);
        System.out.println(JSONAndEntityConvertUtils.entityToIndexJSONObject(order,true)+"===="+234*0.01);
    }
}
