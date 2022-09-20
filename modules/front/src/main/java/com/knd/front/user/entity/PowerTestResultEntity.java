package com.knd.front.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zm
 */
@Data
@TableName("power_test_result")
public class PowerTestResultEntity extends BaseEntity {

    private String id;

    @ApiModelProperty(value = "力量测试子项ID")
    private String powerTestItemId;

    @ApiModelProperty(value = "测试用户ID")
    private String userId;

    @ApiModelProperty(value = "测试时长(秒)")
    private String totalDuration;

    @ApiModelProperty(value = "完成标准值")
    private String finishedKpi;

    @ApiModelProperty(value = "完成总力")
    private String finishedPower;

    @ApiModelProperty(value = "排序")
    private String sort;
}
