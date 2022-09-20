package com.knd.front.user.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.front.pay.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class TrainDetailDto {

    private String id;

    @ApiModelProperty(value = "计划名称")
    private String programName;

    @ApiModelProperty(value = "循环周数")
    private String trainWeekNum;

    @ApiModelProperty(value = "计划类型")
    private String type;

    @ApiModelProperty(value = "封面图片")
    private ImgDto picAttach;

    @ApiModelProperty(value = "最后一次训练时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime lastTrainTime;

    private List<TrainWeekDetailDto> trainDetailList;


}
