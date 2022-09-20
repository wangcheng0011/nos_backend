package com.knd.pay.common;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


/**
 * Map转二维数组
 *
 */
public class MapArrayUtils {


    /**
     * Map转二维数组
     * @param row Row
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Object[][] getTwoArrayObject(Map map) {
        Object[][] object = null;
        if (map != null && !map.isEmpty()) {
            int size = map.size();
            object = new Object[size][2];

            Iterator iterator = map.entrySet().iterator();
            for (int i = 0; i < size; i++) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                object[i][0] = key;
                object[i][1] = value;
            }
        }
        return object;
    }


    /**
     * 根据指定的keys获取Map中的属性
     * @param map
     * @param keys
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map<String, Object> getMapByExistKeys(Map map, String[] keys) {
        if(map == null ||  map.isEmpty()) {
            return null;
        }

        if(keys == null || keys.length < 1) {
            return null;
        }

        Map<String, Object> resultMap = new LinkedHashMap<String, Object>(keys.length);

        Set<String> set = map.keySet();

        for (String key : set) {
            if(ArrayUtils.contains(keys, key)) {
                resultMap.put(key, map.get(key));
            }
        }
        return resultMap;
    }


    /**
     * 排除指定的keys获取Map中的其它属性
     * @param map
     * @param excludeKeys 排除的keys
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map<String, Object> getMapByExcludeKeys(Map map, String[] excludeKeys) {
        if(map == null ||  map.isEmpty()) {
            return null;
        }

        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        Set<String> set = map.keySet();

        for (String key : set) {
            if(ArrayUtils.contains(excludeKeys, key)) {
                continue;
            }
            resultMap.put(key, map.get(key));
        }

        return resultMap;
    }








}