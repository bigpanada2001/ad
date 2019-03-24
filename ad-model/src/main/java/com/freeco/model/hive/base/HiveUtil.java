package com.freeco.model.hive.base;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HiveUtil {

    public static String parseDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
}
