package com.knd.front.user.dto;


import com.knd.front.train.dto.ActionDto;
import com.knd.front.train.dto.FreeTrainDetailDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @author will
 */
@Data
public class PowerTestItemDto {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "是否使用设备 0否 1是")
    private String useEquipmentFlag;
    @ApiModelProperty(value = "力量")
    private String power;
    @ApiModelProperty(value = "参考标准值")
    private String kpa;
    @ApiModelProperty(value = "时长（秒）")
    private String duration;
    @ApiModelProperty(value = "排序")
    private String sort;
    @ApiModelProperty(value = "动作信息")
    private FreeTrainDetailDto freeTrainDetailDto;


}