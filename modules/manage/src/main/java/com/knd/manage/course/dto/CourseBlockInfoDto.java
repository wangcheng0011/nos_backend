package com.knd.manage.course.dto;

import lombok.Data;

@Data
public class CourseBlockInfoDto {
    //id
    private String id ;
    //block名称
    private String block ;
    //目标组数
    private String aimSetNum ;
    //排序号
    private String sort ;
}
