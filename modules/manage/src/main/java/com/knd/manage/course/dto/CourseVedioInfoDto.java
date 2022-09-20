package com.knd.manage.course.dto;

import com.knd.manage.course.entity.CourseTrainningBlock;
import lombok.Data;

import java.util.List;

@Data
public class CourseVedioInfoDto {
    //课程id
    private String id;
    //课程名称
    private String course;
    //训练课程标识,0:介绍课，1：训练课
    private String trainingFlag;
    //视频总时长-分钟
    private String videoDurationMinutes;
    //视频总时长-秒
    private String videoDurationSeconds;
    //block列表
    private List<CourseTrainningBlock> blocks;
    //视频小节列表
    private List<NodeDto> nodes;

}
