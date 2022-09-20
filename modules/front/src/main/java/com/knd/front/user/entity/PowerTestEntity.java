package com.knd.front.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("power_test")
public class PowerTestEntity extends BaseEntity {

    private String id;
    @ApiModelProperty(value = "性别")
    private String gender;
    @ApiModelProperty(value = "难度id")
    private String difficultyId;
    @ApiModelProperty(value = "总时长")
    private String totalDuration;
    @ApiModelProperty(value = "排序")
    private Integer sort;


    @ApiModelProperty(value = "图标id")
    private String picAttachId;


}
