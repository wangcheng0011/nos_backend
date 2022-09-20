package com.knd.front.food.dto;


import com.knd.front.food.entity.Food;
import lombok.Data;

import java.util.List;

@Data
public class FoodListDto {
    //总数
    private int total;
    //列表
    private List<Food> foodList;

}
