package com.knd.common.basic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JsonUtils {

    public static String beanToJson(Object object) {
        return JSONObject.toJSONString(object);
    }

    public static <T> List<T> jsonToBeanList(String json, Class<T> clazz) {
        return JSONArray.parseArray(json, clazz);
    }

    public static <T> T jsonToBean(String json, Class<T> clazz) {
        return JSONObject.parseObject(json, clazz);
    }

}
