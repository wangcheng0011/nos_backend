package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author zm
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("amap_data")
@ApiModel(value="AmapDataEntity对象", description="")
public class AmapDataEntity extends BaseEntity {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "设备编号")
    private String equipmentNo;

    @ApiModelProperty(value = "省唯一识别码")
    private String provinceAdcode;

    @ApiModelProperty(value = "市唯一识别码")
    private String cityAdcode;

    @ApiModelProperty(value = "区唯一识别码")
    private String districtAdcode;

}
