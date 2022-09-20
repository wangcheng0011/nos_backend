package com.knd.front.live.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Data
public class CreateOrUpdateRoomRequest {
    @Pattern(regexp = "^(1|2)$",message = "操作类型只能是1->新增2->修改")
    @NotBlank(message = "操作类型不能为空")
    @ApiModelProperty(value = "操作类型:1->新增2->修改")
    private String postType;
    @ApiModelProperty(value = "主键Id")
    private String id;

    @NotBlank(message = "房间名称不能为空")
    @Size(max = 20,min = 1 ,message = "名称长度必须在1到20之间")
    @ApiModelProperty(value = "房间名称")
    private String roomName;

    @NotBlank(message = "训练小组Id不能为空")
    @ApiModelProperty(value = "训练小组Id")
    private String trainGroupId;

    //@NotBlank(message = "房间密码不能为空")
    //@Size(max = 4,min = 4,message = "房间密码必须是4位数字")

    @Pattern(regexp = "^\\d{4}$",message = "房间密码必须是4位数字")
    @ApiModelProperty(value = "房间密码")
    private String invitationCode;

    @Pattern(regexp = "^(0|1)$",message = "是否公开只能是0->否1->是")
    @NotBlank(message = "是否公开不能为空")
    @ApiModelProperty(value = "是否公开")
    private String publicFlag;

    @Size(max = 150,message = "简介不能超过150个字符")
    @ApiModelProperty(value = "简介")
    private String description;

    @Max(value = 9,message = "人数在1-9之间")
    @Min(value = 1,message = "人数在1-9之间")
    @ApiModelProperty(value = "人数")
    private Integer memberSize;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginDate;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endDate;
}