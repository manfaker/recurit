package com.shenzhen.recurit.utils;

import java.util.Locale;

public class StringFormatUtils {

    public static String format(String fromat, String... strs) {
        return format(Locale.CHINA, fromat, strs);
    }

    public static String format(Locale locale, String fromat, String... strs) {
        return String.format(locale, fromat, strs);
    }
}
