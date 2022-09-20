package com.knd.manage.basedata.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
/**
 *
 * @author zm
 */
public class VoSavePowerTestItem {

    @ApiModelProperty(value = "动作ID")
    @Size(max = 64, message = "动作ID最大长度为64")
    @NotBlank(message = "动作ID不可空")
    private String actionId;

    @ApiModelProperty(value = "部位ID")
    @Size(max = 64, message = "部位ID最大长度为64")
    @NotBlank(message = "部位ID不可空")
    private String bodyPartId;

    @ApiModelProperty(value = "动作名称")
    @Size(max = 128, message = "动作名称最大长度为1")
    private String actionName;

    @ApiModelProperty(value = "是否使用器材 0否 1是")
    @Size(max = 1, message = "最大长度为1")
    private String useEquipmentFlag;

    @ApiModelProperty(value = "力量值")
    @Size(max = 11, message = "力量值最大长度为32")
    private String power;

    @ApiModelProperty(value = "标准值")
    @Size(max = 11, message = "标准值最大长度为11")
    private String kpa;

    @ApiModelProperty(value = "测试时长(秒)")
    @Size(max = 11, message = "测试时长最大长度为11")
    private String duration;

    @ApiModelProperty(value = "排序")
    @Size(max = 11, message = "排序最大长度为11")
    private String sort;
}
