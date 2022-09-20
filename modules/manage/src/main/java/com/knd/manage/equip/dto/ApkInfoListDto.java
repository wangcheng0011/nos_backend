package com.knd.manage.equip.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApkInfoListDto {
    //总数
    private int total;
    //集合
    private List<ApkInfoDto> introductionCourseList;
}
