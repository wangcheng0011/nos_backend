package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
public class PartHobbyDto {

    private String id;
    private String hobby;
    private String remark;
    private String part;
    private String partId;
}
