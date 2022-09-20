package com.knd.manage.food.dto;

import com.knd.manage.food.entity.BaseFoodType;
import lombok.Data;

import java.util.List;

@Data
public class FoodTypeListDto {
    //总数
    private int total;
    //列表
    private List<BaseFoodType> foodTypeList;

}
