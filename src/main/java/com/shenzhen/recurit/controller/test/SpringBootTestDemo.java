package com.shenzhen.recurit.controller.test;

import com.shenzhen.recurit.application.ProduceApplication;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;

public class SpringBootTestDemo {

    @Autowired
    StringEncryptor stringEncryptor;


    public void myTest(){
        System.out.println(stringEncryptor.encrypt("22222"));
        System.out.println(stringEncryptor.decrypt("I0oA+y/kMRgB+XFU35BQGQ=="));
    }


}
