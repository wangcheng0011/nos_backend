package com.knd.front.food.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class VoQueryFoodRecord {
    //userId从token获取
    private String userId;

    @ApiModelProperty(value = "%Y-%m-%d 日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String date;



}
