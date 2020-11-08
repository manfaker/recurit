package com.shenzhen.recurit.test;

import com.shenzhen.recurit.constant.OrdinaryConstant;
import com.shenzhen.recurit.pojo.UserPojo;
import com.shenzhen.recurit.utils.EncryptBase64Utils;
import com.shenzhen.recurit.utils.StringFormatUtils;

import java.util.ArrayList;
import java.util.List;

public class MyTest {

    public static void main(String[] args) {
        String str = "sdfadsf.xlsx";
        System.out.println(str.indexOf(StringFormatUtils.format("%s%s",".","xlsxs"))+"");
    }
}
