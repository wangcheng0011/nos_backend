package com.knd.front.user.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class UserHealthRequest {

    @Size(max = 64)
    @ApiModelProperty(value = "目标体重")
    private String targetWeight;

    @Size(max = 64)
    @ApiModelProperty(value = "当前体重")
    @NonNull
    private String currentWeight;

    @Size(max = 64)
    @ApiModelProperty(value = "目标身高")
    private String targetHeight;

    @Size(max = 64)
    @ApiModelProperty(value = "身高")
    @NonNull
    private String height;

    @Size(max = 64)
    @ApiModelProperty(value = "目标胸围")
    private String targetBust;

    @Size(max = 64)
    @ApiModelProperty(value = "胸围")
    private String bust;

    @Size(max = 64)
    @ApiModelProperty(value = "目标腰围")
    private String targetWaist;

    @Size(max = 64)
    @ApiModelProperty(value = "腰围")
    private String waist;

    @Size(max = 64)
    @ApiModelProperty(value = "目标臀围")
    private String targetHipline;

    @Size(max = 64)
    @ApiModelProperty(value = "臀围")
    private String hipline;

    @Size(max = 64)
    @ApiModelProperty(value = "目标臂围")
    private String targetArmCircumference;

    @Size(max = 64)
    @ApiModelProperty(value = "臂围")
    private String armCircumference;

    @Size(max = 64)
    @ApiModelProperty(value = "当前bmi")
    private String bmi;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "时间",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate date;
}
