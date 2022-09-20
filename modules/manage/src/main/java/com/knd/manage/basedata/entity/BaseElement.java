package com.knd.manage.basedata.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zm
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_element")
@ApiModel(value="BaseElement对象", description="")
public class BaseElement extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "楼层id")
    private String floorId;

    @ApiModelProperty(value = "key值")
    private String keyValue;

    @ApiModelProperty(value = "显示顺序")
    private String sort;

    @ApiModelProperty(value = "元素名称")
    private String elementName;

    @ApiModelProperty(value = "元素详情")
    private String elementDetail;

    @ApiModelProperty(value = "元素备注")
    private String elementNote;

    @ApiModelProperty(value = "图片id")
    private String imageUrlId;

    @ApiModelProperty(value = "背景图片id")
    private String backgroundUrlId;

    @ApiModelProperty(value = "跳转URL")
    private String skipUrl;

    @ApiModelProperty(value = "动态查询URL")
    private String searchUrl;

    @ApiModelProperty(value = "后台展示图")
    private String showUrlId;

    @ApiModelProperty(value = "后台展示说明")
    private String description;

}
