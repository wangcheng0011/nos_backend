package com.knd.front.train.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author will
 */
@Data
public class TrainProgramWeekDetailRequest {

    @ApiModelProperty(value = "周训练日期")
    @NotNull(message = "周训练日期不能为空")
    private Integer weekDayName;
    @Valid
    private List<TrainProgramDayItemRequest> trainProgramDayItemRequests;


}
