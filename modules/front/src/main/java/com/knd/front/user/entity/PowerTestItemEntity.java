package com.knd.front.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("power_test_item")
public class PowerTestItemEntity extends BaseEntity {

    private String id;
    @ApiModelProperty(value = "测试主表id")
    private String powerTestId;
    @ApiModelProperty(value = "动作Id")
    private String actionId;
    @ApiModelProperty(value = "动作Id")
    private String actionName;
    @ApiModelProperty(value = "部位id")
    private String bodyPartId;
    @ApiModelProperty(value = "是否使用器材0否1是")
    private String useEquipmentFlag;
    @ApiModelProperty(value = "力量值")
    private String power;
    @ApiModelProperty(value = "标准值")
    private String kpa;
    @ApiModelProperty(value = "测试时长")
    private String duration;

    @ApiModelProperty(value = "排序")
    private String sort;
   

}
