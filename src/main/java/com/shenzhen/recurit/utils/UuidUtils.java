package com.shenzhen.recurit.utils;

import com.shenzhen.recurit.constant.OrdinaryConstant;

import java.util.UUID;

public class UuidUtils {

    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll(OrdinaryConstant.SYMBOL_2,OrdinaryConstant.IS_BLACK);
    }
}
