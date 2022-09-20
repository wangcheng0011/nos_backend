package com.knd.common.em;

import com.knd.common.utils.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Optional;
/**
 * platform
 * @author will
 */
@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
public enum PlatformEnum {
    ANDROID_PLATFORM("10","android"),
    IOS_PLATFORM("11","ios"),
    //jsapi支付
    JSAPI_PLATFORM("22","jsapi"),
    //大屏
    QUINNOID("33","quinnoid"),
    APP("44","app"),
    //小程序支付
    SMALL_ROUTINE("55","smallRoutine"),
    WEBSITE("66","website"),
    WX("77","wx");

    //
    private final String code;
    private final String name;

    public static PlatformEnum getEnumByCode(String code){
        Optional<PlatformEnum> m1 = EnumUtil.getEnumObject(PlatformEnum.class, e -> e.getCode().equals(code));
        return m1.isPresent() ? m1.get() : null;
    }

    public static String getNameByCode(String code){
        Optional<PlatformEnum> m1 = EnumUtil.getEnumObject(PlatformEnum.class, e -> e.getCode().equals(code));
        return m1.isPresent() ? m1.get().getName() : null;
    }


}
