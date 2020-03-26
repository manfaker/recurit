package com.shenzhen.recurit.controller.test;

import com.shenzhen.recurit.service.impl.UserServiceImpl;

import java.util.Calendar;

public class MyTest {

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.MONTH)+"==="+calendar.get(Calendar.DATE) +"==="+calendar.get(Calendar.DAY_OF_MONTH));
    }
}
