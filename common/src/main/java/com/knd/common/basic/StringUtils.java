package com.knd.common.basic;

import com.knd.common.utils.RandomNumberUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 字符串工具类
 *
 * @author wcy
 */
public class StringUtils extends org.springframework.util.StringUtils {

    /**
     *
     */
    public static final String EMPTY = "";


    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        Boolean a = !StringUtils.isEmpty(str) && !str.trim().equals("");

        return !StringUtils.isEmpty(str) && !str.trim().equals("");
    }

    /**
     * 判断字符串是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        if (obj instanceof String[]) {
            if (((String[]) obj).length == 0) {
                return false;
            }

        } else if (obj instanceof Collection) {
            if (((Collection) obj).size() == 0) {
                return false;
            }

        } else {
            if (obj == null || obj.equals("") || obj.toString().trim().equals("")) {
                return false;
            }

        }
        return true;
    }

    /**
     * 判断字符串是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj instanceof String[]) {
            if (((String[]) obj).length == 0) {
                return true;
            }

        } else if (obj instanceof Collection) {
            if (((Collection) obj).size() == 0) {
                return true;
            }

        } else {
            if (obj == null || obj.equals("") || obj.toString().trim().equals("")) {
                return true;
            }

        }
        return false;
    }

    /**
     * @param str
     * @return
     * @throws
     * @Title: getStrValue
     * @Description: 进行Null处理
     * @return: String
     */
    public static String getStrValue(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param array
     * @return
     */
    public static boolean isEmpty(String... array) {
        boolean flag = false;
        if (array != null && array.length > 0) {
            for (String str : array) {
                flag = StringUtils.isEmpty(str);
                if (flag) {
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 判断List是否为空
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    /**
     * 判断List是否为空
     *
     * @param list
     * @return
     */
    public static boolean isNotEmpty(List<?> list) {
        return !StringUtils.isEmpty(list);
    }

    /**
     * 判断Map是否为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    /**
     * 判断object
     *
     * @param obj
     * @return
     */
    public static boolean isObjEmpty(Object obj) {
        return StringUtils.isEmpty(obj) || obj.toString().trim().equals("");
    }

    /**
     * 判断Map是否为空
     *
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !StringUtils.isEmpty(map);
    }


    public static boolean isNotEmptyObjects(Object... objs) {
        if (objs instanceof String[]) {
            if (StringUtils.isEmpty(objs)) {
                return false;
            }

        } else {
            for (Object obj : objs) {
                if (StringUtils.isEmpty(obj)) {
                    return false;
                }


            }

        }
        return true;

    }

    public static boolean isAllNotEmptyObjects(Object... objs) {

        if (objs instanceof String[]) {
            if (StringUtils.isNotEmpty(objs)) {
                return true;
            }

        } else {
            for (Object obj : objs) {
                if (StringUtils.isNotEmpty(obj)) {
                    return true;
                }


            }

        }
        return false;

    }

    public static String upperFirst(String str){
        if(StringUtils.isEmpty(str)) {
            return "";
        } else {
            return str.substring(0,1).toUpperCase() + str.substring(1);
        }
    }

    /**
     * 要求外部订单号必须唯一。
     * @return {String}
     */
    public  static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        for (int i = 0; i < 4; i++) {
            key += new Random().nextInt(10);
        }
        return key;
    }

    public static Integer stringToInt(String str){
        int idx = str.lastIndexOf(".");//查找小数点的位置
        if(idx!=-1){
            String strNum = str.substring(0,idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
            return Integer.valueOf(strNum);
        }
        return Integer.valueOf(str);
    }

    public static Double StringToDouble(String str){
        DecimalFormat df = new DecimalFormat("0.00");
        Double dd=Double.parseDouble(str);
        String ff=df.format(dd);
        return Double.parseDouble(ff);
    }

}
