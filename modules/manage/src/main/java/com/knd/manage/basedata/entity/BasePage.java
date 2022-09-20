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
@TableName("base_page")
@ApiModel(value="BasePage对象", description="")
public class BasePage extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "key值")
    private String keyValue;

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "页面类型")
    private String pageType;

    @ApiModelProperty(value = "页面名称")
    private String pageName;

    @ApiModelProperty(value = "页面详情")
    private String pageDetail;

    @ApiModelProperty(value = "图片id")
    private String imageUrlId;

    @ApiModelProperty(value = "背景图片id")
    private String backgroundUrlId;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "跳转URL")
    private String skipUrl;

    @ApiModelProperty(value = "动态查询URL")
    private String searchUrl;

    @ApiModelProperty(value = "后台展示图")
    private String showUrlId;

    @ApiModelProperty(value = "后台展示说明")
    private String description;

}
