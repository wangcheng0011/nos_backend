package com.knd.manage.course.dto;

import com.knd.manage.course.entity.TrainProgramEntity;
import com.knd.manage.equip.entity.TestReportEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;
/**
 * @author Lenovo
 */
@Data
@Builder
public class TrainListDto {

    //总数
    private int total;
    //集合
    private List<TrainProgramEntity> trainList;
}
