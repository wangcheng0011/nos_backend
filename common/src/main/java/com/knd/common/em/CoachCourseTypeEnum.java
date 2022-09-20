package com.knd.common.em;


/**
 * @author zm
 * 标签类别
 * 目前前5项为教练类型
 */

public enum CoachCourseTypeEnum {

    COURSE_CONSULT("课前咨询"),
    COURSE("私教课程"),
    LIVE("团课直播");

    private String display;

    CoachCourseTypeEnum(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return this.display;
    }
}
