package com.knd.front.train.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author will
 */
@Data
public class GetTrainProgramRequest {

    @ApiModelProperty(value = "计划名称")
    private String programName;
    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "")//用户id不能为空
    private String userId;
    @ApiModelProperty(value = "训练计划id")
    private String trainProgramId;
    @ApiModelProperty(value = "计划开始时间,格式为yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginDate;
    @ApiModelProperty(value = "计划结束时间,格式为yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endDate;

}
