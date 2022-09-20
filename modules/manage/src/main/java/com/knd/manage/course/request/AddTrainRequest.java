package com.knd.manage.course.request;

import com.knd.manage.basedata.vo.VoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class AddTrainRequest {
    private String userId;

    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;

    @Size(max = 64)
    private String programId;

    @ApiModelProperty(value = "计划名称")
    @NotBlank(message = "计划名称不能为空")
    private String programName;

    @ApiModelProperty(value = "计划类型")
    @NotBlank
    private String type;

    @ApiModelProperty(value = "循环周数")
    @NotNull(message = "循环周数不能为空")
    private String trainWeekNum;

    @ApiModelProperty(value = "图片")
    private VoUrl picAttachUrl;

    @Valid
    private List<TrainDetailRequest> detailList;
}
