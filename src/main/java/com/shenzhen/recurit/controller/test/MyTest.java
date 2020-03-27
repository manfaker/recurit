package com.shenzhen.recurit.controller.test;

import com.shenzhen.recurit.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyTest {

    public static void main(String[] args) {
        List<String> listStr = new ArrayList<>();
        listStr.add("12333");
        listStr.add("12333");
        listStr.add("222222");
        listStr.add("222222");
        listStr.add("12333");
        listStr.add("12333");
        for(int index=listStr.size()-1;index>=0;index--){
            String str = listStr.get(index);
            if(str.equals("12333")){
                listStr.remove(str);
            }
        }
        System.out.println(listStr.toString());
    }
}
