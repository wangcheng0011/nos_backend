package com.knd.manage.course.vo;

import lombok.Data;

import java.util.List;

@Data
public class VoCourseHeadList {
    private String id;
    private String course;
    private String sort;
    private String releaseFlag;
    private String appHomeFlag;
    private String userScope;
    private String releaseTime;
    private String types;
    private String targets;
    private String parts;
    private List<VoType> voTypeList;
    private List<VoTarget> voTargetList;
    private List<VoPart> voPartList;

}
