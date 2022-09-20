package com.knd.manage.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class PowerLevelTestListDto {
    //总数
    private int total;
    //集合
    private List<PowerLevelTestDto> powerLevelTestList;
}
