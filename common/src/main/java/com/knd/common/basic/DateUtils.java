package com.knd.common.basic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;

public class DateUtils {

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMATTER_YYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");


    /**
     * 返回当前的日期DATE_FORMATTER
     *
     * @return
     */
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

    /**
     * 返回当前时间
     *
     * @return
     */
    public static LocalTime getCurrentLocalTime() {
        return LocalTime.now();
    }

    /**
     * 返回当前日期时间
     *
     * @return
     */
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 当前时间字符串（yyyy-MM-dd）
     *
     * @return
     */
    public static String getCurrentDateStr() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    /**
     * 当前时间字符串（yyyy-MM）
     *
     * @return
     */
    public static String getCurrentMonthStr() {
        return LocalDate.now().format(MONTH_FORMATTER);
    }

    /**
     * 当前时间字符串（yyyy-MM-dd HH:mm:ss）
     *
     * @return
     */
    public static String getCurrentDateTimeStr() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }

    /**
     * 当前时间字符串（HH:mm:ss）
     *
     * @return
     */
    public static String getCurrentTimeStr() {
        return LocalTime.now().format(TIME_FORMATTER);
    }

    /**
     * 自定义格式获取当前时间(yyyy-MM-dd)
     *
     * @param pattern 格式
     * @return
     */
    public static String getCurrentDateStr(String pattern) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * @param pattern
     * @return
     */
    public static String getCurrentDateTimeStr(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * @param pattern
     * @return
     */
    public static String getCurrentTimeStr(String pattern) {
        return LocalTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * @param dateStr
     * @param pattern
     * @return
     */
    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * @param dateTimeStr
     * @param pattern
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * @param timeStr
     * @param pattern
     * @return
     */
    public static LocalTime parseLocalTime(String timeStr, String pattern) {
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * @param date
     * @param pattern
     * @return
     */
    public static String formatLocalDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * @param datetime
     * @param pattern
     * @return
     */
    public static String formatLocalDateTime(LocalDateTime datetime, String pattern) {
        return datetime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * @param time
     * @param pattern
     * @return
     */
    public static String formatLocalTime(LocalTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * @param dateStr
     * @return
     */
    public static LocalDate parseLocalDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    /**
     * @param dateTimeStr
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
    }

    /**
     * @param timeStr
     * @return
     */
    public static LocalTime parseLocalTime(String timeStr) {
        return LocalTime.parse(timeStr, TIME_FORMATTER);
    }

    /**
     * @param date
     * @return
     */
    public static String formatLocalDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    /**
     * @param datetime
     * @return
     */
    public static String formatLocalDateTime(LocalDateTime datetime) {
        return datetime.format(DATETIME_FORMATTER);
    }

    /**
     * @param time
     * @return
     */
    public static String formatLocalTime(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }

    /**
     * 日期相隔天数
     *
     * @param startDateInclusive
     * @param endDateExclusive
     * @return
     */
    public static long periodDays(LocalDate startDateInclusive, LocalDate endDateExclusive) {
        return endDateExclusive.toEpochDay() - startDateInclusive.toEpochDay();
    }

    /**
     * 日期相隔小时
     *
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public static long durationHours(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toHours();
    }

    /**
     * 日期相隔分钟
     *
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public static long durationMinutes(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toMinutes();
    }

    /**
     * 日期相隔毫秒数
     *
     * @param startInclusive
     * @param endExclusive
     * @return
     */
    public static long durationMillis(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive).toMillis();
    }

    /**
     * 是否当天
     *
     * @param date
     * @return
     */
    public static boolean isToday(LocalDate date) {
        return getCurrentLocalDate().equals(date);
    }

    /**
     * 获取此日期时间与默认时区<Asia/Shanghai>组合的时间毫秒数
     *
     * @param dateTime
     * @return
     */
    public static Long toEpochMilli(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取此日期时间与指定时区组合的时间毫秒数
     *
     * @param dateTime
     * @return
     */
    public static Long toSelectEpochMilli(LocalDateTime dateTime, ZoneId zoneId) {
        return dateTime.atZone(zoneId).toInstant().toEpochMilli();
    }

    /**
     * 两个时间比较
     *
     * @param dateBefore
     * @param dateAfter
     * @return
     */
    public static boolean compareTime(LocalDate dateBefore, LocalDate dateAfter) {
        return dateBefore.isAfter(dateAfter);
    }

    /**
     * 指定日期加指定天数
     *
     * @return
     */
    public static Date addDate(Date date, long day) {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day * 24 * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
        time += day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

    /**
     * String转换成Date
     *
     * @return
     */
    public static Date stringToDate(String stringTime) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        Date date = ft.parse(stringTime);
        return date;
    }

    /**
     * Date转换为LocalDateTime
     *
     * @param date
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();//An instantaneous point on the time-line.(时间线上的一个瞬时点。)
        ZoneId zoneId = ZoneId.systemDefault();//A time-zone ID, such as {@code Europe/Paris}.(时区)
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

        return localDateTime;
    }

    /**
     * 传入毫秒转为时间(不包括日期)
     *
     * @param time
     * @return
     */
    public static String getGapTime(long time) {

        long hours = time / (1000 * 60 * 60);
        long minutes = (time - hours * (1000 * 60 * 60)) / (1000 * 60);
        long second = (time-minutes*(1000*60))/1000;


        String diffTime = "";
        if (hours >= 24) {
            if (minutes<10){
                diffTime =   hours + "小时0" + minutes + "分钟"+second+"秒";
            }else{
                diffTime =   hours + "小时" + minutes + "分钟"+second+"秒";
            }
        } else if (hours >= 1) {
            if (minutes < 10) {
                diffTime = hours + "小时0" + minutes + "分钟"+second+"秒";
            } else {
                diffTime = hours + "小时" + minutes + "分钟"+second+"秒";
            }
        } else {
            if (minutes < 1) {
                diffTime = second+"秒";
            }else if(minutes < 10){
                diffTime = "0" + minutes + "分钟"+second+"秒";
            }
            else {
                diffTime = minutes + "分钟"+second+"秒";
            }
        }

        return diffTime;
    }

    /**
     * 传入秒转为分钟
     * @return
     */
    public static String getMin(long second){
        long s = second *1000;
        String diffTime = "";
        if (second<=60){
            diffTime = "1";
        }else {
            diffTime = s/60000+"";
        }
        return diffTime;
    }


}
