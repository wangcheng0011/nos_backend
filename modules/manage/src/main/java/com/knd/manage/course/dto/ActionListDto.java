package com.knd.manage.course.dto;

import com.knd.manage.course.entity.ActionArrayEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
@Builder
public class ActionListDto {

    //总数
    private int total;
    //集合
    private List<ActionArrayEntity> actionArrayList;
}
