package com.shenzhen.recurit.utils;

import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.enums.NumberEnum;

public class RandomUtils {

    public static String randomStr(int length){
        if(length< NumberEnum.SIX.getValue()){
            length = NumberEnum.SIX.getValue();
        }
        StringBuilder builder = new StringBuilder(OrdinaryConstant.IS_BLACK);
        for(int index=NumberEnum.ZERO.getValue();index<length;index++){
                if((((int)(Math.random()*NumberEnum.TEN.getValue()))%NumberEnum.TWO.getValue())==NumberEnum.ZERO.getValue()){
                    builder.append(getRandomChar());
                }else{
                    builder.append((int)Math.random()*NumberEnum.TEN.getValue());
                }
        }
        return builder.toString();
    }

    private static char getRandomChar(){
        int intNumber = NumberEnum.ZERO.getValue() ;
        if((((int)(Math.random()*NumberEnum.TEN.getValue()))%NumberEnum.TWO.getValue())==NumberEnum.ZERO.getValue()){
            //生成一个97-122之间的int类型整数--为了生成小写字母
            intNumber = (int)(Math.random()*NumberEnum.TWENTY_SIX.getValue()+NumberEnum.NINETY_SEVEN.getValue());
        }else{
            //生成一个65-90之间的int类型整数--为了生成大写字母
            intNumber  = (int)(Math.random()*NumberEnum.TWENTY_SIX.getValue()+NumberEnum.SIXTY_FIVE.getValue());
        }
        return (char)intNumber;
    }

    public static void main(String[] args) {
        System.out.println((int)(Math.random()*NumberEnum.TEN.getValue()));
    }
}
