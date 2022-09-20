package com.knd.manage.course.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.manage.basedata.vo.VoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author will
 */
@Data
@Builder
public class SaveTrainProgramRequest {

    @ApiModelProperty(value = "计划名称")
    private String programName;

    @ApiModelProperty(value = "用户id")
//    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(value = "计划开始时间")
//    @NotBlank(message = "计划开始时间不能为空，格式为yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String beginTime;

    @ApiModelProperty(value = "循环周数")
    @NotNull(message = "循环周数不能为空")
    private Integer trainWeekNum;

    @ApiModelProperty(value = "0系统添加 1用户添加")
    @NotBlank(message = "数据来源不能为空")
    private String source;

    @ApiModelProperty(value = "类型：0专项增肌 1有氧燃脂 2大咖代练 3新手体验")
    private String type;

    @ApiModelProperty(value = "图片")
    private VoUrl picAttachUrl;

    @Valid
    private List<TrainProgramWeekDetailRequest> trainProgramWeekDetailRequests;

}
