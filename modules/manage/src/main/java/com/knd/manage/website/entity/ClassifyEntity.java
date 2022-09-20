package com.knd.manage.website.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangcheng
 */
@Data
@TableName("base_classify")
public class ClassifyEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "分类")
    private String classify;

}