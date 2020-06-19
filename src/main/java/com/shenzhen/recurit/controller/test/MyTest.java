package com.shenzhen.recurit.controller.test;


import com.shenzhen.recurit.pojo.ExportsPojo;
import com.shenzhen.recurit.utils.EncryptBase64Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyTest {

    public static void main(String[] args) {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simple.format(new Date()));
    }
}
