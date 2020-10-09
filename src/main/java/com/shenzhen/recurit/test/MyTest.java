package com.shenzhen.recurit.test;

import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.pojo.UserPojo;
import com.shenzhen.recurit.utils.EncryptBase64Utils;

import java.util.ArrayList;
import java.util.List;

public class MyTest {

    public static void main(String[] args) {

        System.out.println(EncryptBase64Utils.decryptBASE64("MTc1MTkyMTE2QHFxLmNvbQ=="));
    }
}
