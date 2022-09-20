package com.knd.front.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zm
 */

@ApiModel(value = "用户购买类型")
public enum UserPayCheckEnum {

    /**
     * 课程
     */
    @ApiModelProperty("课程")
    COURSE("课程");

    private String display;

    UserPayCheckEnum(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return this.display;
    }
}
