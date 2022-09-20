package com.knd.manage.basedata.dto;

import lombok.Data;

import java.util.List;

@Data
public class ActionListDto {
    //总数
    private int total;
    //列表
    private List<ActionInfoDto> actionList;
}
