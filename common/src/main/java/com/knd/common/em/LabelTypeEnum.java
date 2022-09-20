package com.knd.common.em;


/**
 * @author zm
 * 标签类别
 * 目前前5项为教练类型
 */

public enum LabelTypeEnum {
    BODYBUILDING("健美"),
    POWER("力量"),
    YOGA("瑜伽"),
    DANCE("舞蹈"),
    PILATES("普拉提"),
    STRENGTH("体能");

    private String display;

    LabelTypeEnum(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return this.display;
    }
}
