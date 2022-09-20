package com.knd.manage.homePage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wc
 */
@Data
@EqualsAndHashCode()
@TableName("amap_adcode")
@ApiModel(value="AmapAdCodeEntity", description="")
public class AmapAdCodeEntity {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "行政编码")
    private String adcode;

    @ApiModelProperty(value = "邮政编码")
    private String citycode;


}
