package com.knd.redis.cache;

import java.util.HashMap;

public enum RedisCacheEnum {
    CREATE(0),
    UPDATE(1),
    DELETE(2);
    private int value;

    RedisCacheEnum(int v) {
        value = v;
    }

    private static HashMap<Integer, RedisCacheEnum> map = new HashMap<>();

    static {
        for (RedisCacheEnum deleteEnum : RedisCacheEnum.values()) {
            map.put(deleteEnum.getValue(), deleteEnum);
        }
    }

    public int getValue() {
        return value;
    }

    public static RedisCacheEnum getName(int value) {
        return map.get(value);
    }

}
