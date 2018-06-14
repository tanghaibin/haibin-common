package com.haibin.common.util;

import org.codelogger.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 压缩日期长度
 * @author haibin.tang
 */
public class CompressDateUtil {
    /**
     * 年 对应编码
     */
    private static Map<Integer, String> yearEncode;
    /**
     * 月 对应编码
     */
    private static Map<Integer, String> monthEncode;
    /**
     * 日 对应编码
     */
    private static Map<Integer, String> dayEncode;
    /**
     * 时 对应编码
     */
    private static Map<Integer, String>  hourEncode;
    /**
     * 分 秒 对应编码
     */
    private static Map<Integer, String>  minuteEncode;
    /**
     * 起始年份
     */
    private static final Integer START_YEAR = 2016;

    private static final String [] YEAR_ENCODE_CODE = new String[]{
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
    };

    private static final String[] MONTH_ENCODE_CODE = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "a", "b"
    };

    private static final String [] DAY_ENCODE_CODE = new String[]{
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "1", "2", "3", "4", "5"
    };

    private static final String [] HOUR_ENCODE_CODE = new String[]{
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x"
    };

    private static final String [] MINUTE_ENCODE_CODE = new String[]{
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "1", "2", "3", "4", "5", "6", "7", "8"
    };

    static {
        initYearEncode();
        initMonthEncode();
        initDayEncode();
        initHourEncode();
        initMinuteEncode();
    }

    private static void initYearEncode() {
        yearEncode = new HashMap<>();
        int currentYear = START_YEAR;
        for (int index = 1; index <= YEAR_ENCODE_CODE.length; ++index) {
            yearEncode.put(currentYear++, YEAR_ENCODE_CODE[index - 1]);
        }
    }

    private static void initMonthEncode() {
        monthEncode = new HashMap<>();
        for (int index = 1; index <= MONTH_ENCODE_CODE.length; ++index) {
            monthEncode.put(index, MONTH_ENCODE_CODE[index - 1]);
        }
    }

    private static void initDayEncode() {
        dayEncode = new HashMap<>();
        for (int index = 1; index <= DAY_ENCODE_CODE.length; ++index) {
            dayEncode.put(index, DAY_ENCODE_CODE[index - 1]);
        }
    }

    private static void initHourEncode() {
        hourEncode = new HashMap<>();
        for (int index = 0; index < HOUR_ENCODE_CODE.length; ++index) {
            hourEncode.put(index, HOUR_ENCODE_CODE[index]);
        }
    }

    private static void initMinuteEncode() {
        minuteEncode = new HashMap<>();
        for (int index = 0; index < MINUTE_ENCODE_CODE.length; ++index) {
            minuteEncode.put(index, MINUTE_ENCODE_CODE[index]);
        }
    }

    /**
     * 对日期进行编码
     *  日期必须精确到  毫秒
     * @param date
     * @return
     */
    public static String encode(Date date) {
        if(date == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        result.append(yearEncode.get(calendar.get(Calendar.YEAR)));
        result.append(monthEncode.get(calendar.get(Calendar.MONTH) + 1));
        result.append(dayEncode.get(calendar.get(Calendar.DAY_OF_MONTH)));
        result.append(hourEncode.get(calendar.get(Calendar.HOUR_OF_DAY)));
        result.append(minuteEncode.get(calendar.get(Calendar.MINUTE)));
        result.append(minuteEncode.get(calendar.get(Calendar.SECOND)));
        result.append(calendar.get(Calendar.MILLISECOND));
        return result.toString();
    }

    public static void main(String[] args) {
//        System.out.println(new Date().getDate());
//        System.out.println(new Date().getMonth());
//        System.out.println(new Date().getTimezoneOffset());

//        for (int i = 0; i < 10; ++i) {
//            System.out.println(encode(new Timestamp(System.currentTimeMillis())));
//        }

        Date date = new Date();

        StringBuilder result = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        result.append(yearEncode.get(calendar.get(Calendar.YEAR)));
        result.append(monthEncode.get(calendar.get(Calendar.MONTH) + 1));
        result.append(dayEncode.get(calendar.get(Calendar.DAY_OF_MONTH)));
        result.append(hourEncode.get(calendar.get(Calendar.HOUR_OF_DAY)));
        result.append(minuteEncode.get(calendar.get(Calendar.MINUTE)));
        result.append(minuteEncode.get(calendar.get(Calendar.SECOND)));
        result.append(calendar.get(Calendar.MILLISECOND));
        System.out.println(calendar.get(Calendar.MILLISECOND));
        System.out.println(result.toString());

        System.out.println(date);
        System.out.println(encode(date));
        System.out.println(encode(DateUtils.getDateFromString("2017-10-10 00:00:00:134", "yyyy-MM-dd HH:mm:ss:SSS")));
    }
}
