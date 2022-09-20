package com.knd.manage.basedata.dto;


import com.knd.manage.basedata.entity.BaseTarget;
import lombok.Data;

import java.util.List;

@Data
public class TargetListDto {
    //总数
    private int total;
    //列表
    private List<BaseTarget> targetList;
}
