package com.knd.front.train.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.front.dto.VoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author will
 */
@Data
public class SaveTrainProgramRequest {

    @ApiModelProperty(value = "计划名称")
    private String programName;

    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(value = "计划开始时间")
//  @NotBlank(message = "计划开始时间不能为空，格式为yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "循环周数")
    @NotNull(message = "循环周数不能为空")
    private Integer trainWeekNum;

    @ApiModelProperty(value = "图片")
    private VoUrl picAttachUrl;

    @ApiModelProperty(value = "0系统添加 1用户添加")
    @NotBlank(message = "数据来源不能为空")
    private String source;

    @ApiModelProperty(value = "类型：0专项增肌 1有氧燃脂 2高效塑形 3新手体验")
    private String type;

    @Valid
    private List<TrainProgramWeekDetailRequest> trainProgramWeekDetailRequests;

}
