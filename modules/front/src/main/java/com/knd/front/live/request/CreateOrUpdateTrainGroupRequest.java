package com.knd.front.live.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Data
public class CreateOrUpdateTrainGroupRequest {
    @Pattern(regexp = "^(1|2)$",message = "操作类型只能是1->新增2->修改")
    @NotBlank(message = "操作类型不能为空")
    @ApiModelProperty(value = "操作类型:1->新增2->修改")
    private String postType;

    @ApiModelProperty(value = "主键Id")
    private String id;

    @NotBlank(message = "小组名称不能为空")
    //@Size(max = 20,min = 3)
    @ApiModelProperty(value = "小组名称")
    private String groupName;

    @NotBlank(message = "健身目标不能为空")
    @ApiModelProperty(value = "健身目标Id")
    private String targetId;

    @NotBlank(message = "爱好不能为空")
    @ApiModelProperty(value = "爱好Id")
    private String partHobbyId;

//    @NotBlank(message = "房间密码不能为空")
//    @Size(max = 4,min = 4,message = "房间密码必须是4位数字")
//    private Integer invitationCode;

    @Pattern(regexp = "^(0|1)$",message = "是否公开只能是0->否1->是")
    @NotBlank(message = "是否公开不能为空")
    @ApiModelProperty(value = "是否公开:0->否1->是")
    private String publicFlag;

    @Size(max = 150,message = "简介不能超过150个字符")
    @ApiModelProperty(value = "简介")
    private String description;

    @Max(value = 9,message = "人数在1-9之间")
    @Min(value = 1,message = "人数在1-9之间")
    @ApiModelProperty(value = "人数")
    private Integer memberSize;

    @ApiModelProperty(value = "器材Id")
    private String equipmentId;
}