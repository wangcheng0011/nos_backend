package com.knd.manage.website.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangcheng
 */
@Data
public class ClassifyRequest {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "分类")
    private String classify;

}