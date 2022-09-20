/**
 *
 */
package com.knd.common.uuid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * UUID辅助工具类
 *
 * @author wcy
 *
 */
public class UUIDUtil {

    /**
     *
     */
    private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    /**
     * 获取UUID
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取短UUID
     * @return
     */
    public static String getShortUUID() {
        StringBuffer shortBuffer = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            String str = getUUID().substring(i * 4, i * 4 + 4);
            shortBuffer.append(chars[Integer.parseInt(str, 16) % 0x3E]);
        }
        return shortBuffer.toString();
    }


    public static String getNumUUID() {
        StringBuffer shortBuffer = new StringBuffer();
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        shortBuffer.append(formatter.format(currentTime));
        int number1 = (int) (Math.random() * 10);
        shortBuffer.append(number1);
        int number2 = (int) (Math.random() * 10);
        shortBuffer.append(number2);
        int number3 = (int) (Math.random() * 10);
        shortBuffer.append(number3);
        return shortBuffer.toString();
    }

}
