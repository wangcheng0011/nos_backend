package com.knd.manage.basedata.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
//@ApiModel(value="力量等级计数目标值列表对象", description="")
public class VoAimSetting {
    @ApiModelProperty(value = "计数力量等级")
    @NotBlank(message = "计数力量等级不可空")
    private String powerLevel;
    @ApiModelProperty(value = "计数目标时长")
    @NotBlank(message = "计数目标时长不可空")
    private String aimDuration;
    @ApiModelProperty(value = "计数目标次数")
    @NotBlank(message = "计数目标次数不可空")
    private String aimTimes;
    @ApiModelProperty(value = "基础力")
    @NotBlank(message = "基础力不可空")
    @Pattern(regexp = "^([3-9])|([1-3][0-9])|([4][0-5])$",message = "基础力参数错误")
    //范围是 [3,45]
    private String basePower;

}
