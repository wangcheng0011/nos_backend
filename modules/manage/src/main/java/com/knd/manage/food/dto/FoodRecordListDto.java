package com.knd.manage.food.dto;

import com.knd.manage.food.entity.FoodRecord;
import lombok.Data;

import java.util.List;

@Data
public class FoodRecordListDto {
    //总数
    private int total;
    //列表
    private List<FoodRecord> foodRecordList;

}
