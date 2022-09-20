package com.knd.manage.course.dto;

import com.knd.manage.basedata.dto.EquipmentDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CourseHeadDto {
    //课程id
    private String id;
    //课程名称
    private String course;
    //训练课程标识
    private String trainingFlag;
    //视频介绍描述
    private String remark;
    //适用人群介绍
    private String fitCrowdRemark;
    //禁忌人群介绍
    private String unFitCrowdRemark;
    //是否在app大屏显示
    private String appHomeFlag;
    //显示顺序
    private String sort;
    //适用会员范围
    private String userScope;
    //频率
    private String frequency;

    //视频总时长-分钟
    private String videoDurationMinutes;
    //视频总时长-秒
    private String videoDurationSeconds;

    //发布状态
    private String releaseFlag;
    //分类标签列表
    private List<TypeDto> typeList;
    //设备列表
    private List<EquipmentDto> equipmentList;
    //目标列表
    private List<TargetDto> targetList;
    //部位标签列表
    private List<PartDto> partList;

//    //介绍视频id
//    private String videoAttachId;
    //介绍视频Url
    private String videoAttachUrl;
//    //封面图片id
//    private String picAttachId;
    //封面图片url
    private String picAttachUrl;

//
    //介绍视频原名称
    private String videoAttachName;
    //介绍视频新名称
    private String videoAttachNewName;
    //介绍视频大小
    private String videoAttachSize;
    //封面图片原名称
    private String picAttachName;
    //封面图片新名称
    private String picAttachNewName;
    //封面图片大小
    private String picAttachSize;

    private String amount;

    private String courseType;

    private String difficultyId;


}
