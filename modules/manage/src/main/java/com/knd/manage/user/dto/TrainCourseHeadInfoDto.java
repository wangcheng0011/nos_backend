package com.knd.manage.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/8/17
 * @Version 1.0
 */
@Data
public class TrainCourseHeadInfoDto {
    private String id;
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String commitTrainTime;
    private String course;
    private String totalDurationSeconds;
    private String actualTrainSeconds;
    private List<TrainCourseBlockInfoDto> blockList;



}

