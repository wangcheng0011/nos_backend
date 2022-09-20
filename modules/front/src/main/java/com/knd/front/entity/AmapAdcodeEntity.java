package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zm
 */
@Data
@TableName("amap_adcode")
@ApiModel(value="AmapAdcodeEntity对象", description="")
public class AmapAdcodeEntity {

    private String id;

    @ApiModelProperty(value = "地区名称")
    private String name;

    @ApiModelProperty(value = "地区代码")
    private String adcode;

    @ApiModelProperty(value = "城市代码")
    private String citycode;
}
