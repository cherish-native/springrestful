package com.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {

    public static final String PATTERN_YYYYMMDD = "yyyyMMdd";
    public static final String PATTERN_YYYYMMDD_WITH_HORIZONTAL_LINE = "yyyy-MM-dd";
    public static final String PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期转字符串
     * @param date
     * @param pattern
     * @return
     */
    public static String getDateStr(Date date, String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * 字符串转日期
     * @param dateStr
     * @param pattern
     * @return
     * @throws Exception
     */
    public static Date strToDate(String dateStr, String pattern) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(dateStr);
    }
}
