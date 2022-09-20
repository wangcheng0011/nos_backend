package com.knd.manage.user.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author zm
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_health")
@ApiModel(value="UserHealth对象", description="")
public class UserHealthEntity extends BaseEntity {

    @ApiModelProperty(value = "目标体重")
    private String targetWeight;

    @ApiModelProperty(value = "当前体重")
    private String currentWeight;

    @ApiModelProperty(value = "目标身高")
    private String targetHeight;

    @ApiModelProperty(value = "身高")
    private String height;

    @ApiModelProperty(value = "目标胸围")
    private String targetBust;

    @ApiModelProperty(value = "胸围")
    private String bust;

    @ApiModelProperty(value = "目标腰围")
    private String targetWaist;

    @ApiModelProperty(value = "腰围")
    private String waist;

    @ApiModelProperty(value = "目标臀围")
    private String targetHipline;

    @ApiModelProperty(value = "臀围")
    private String hipline;

    @ApiModelProperty(value = "目标臂围")
    private String targetArmCircumference;

    @ApiModelProperty(value = "臂围")
    private String armCircumference;

    @ApiModelProperty(value = "bmi")
    private String bmi;

    private String userId;

    private LocalDate date;
}
