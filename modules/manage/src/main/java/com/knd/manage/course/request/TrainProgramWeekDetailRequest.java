package com.knd.manage.course.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author will
 */
@Data
@Builder
public class TrainProgramWeekDetailRequest {
    //TODO
    //@ApiModelProperty(value = "周训练日期")
    @NotNull(message = "周训练日期不能为空")
    private Integer weekDayName;
    @Valid
    private List<TrainProgramDayItemRequest> trainProgramDayItemRequests;
}
