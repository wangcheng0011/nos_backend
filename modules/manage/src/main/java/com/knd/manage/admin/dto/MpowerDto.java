package com.knd.manage.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class MpowerDto {
    //模块名称
    private String moduleName;
    //模块id
    private String moduleId;
    //页面名称
    private String pageName;
    //页面id
    private String pageId;
    //按钮列表
    private List<ButtonDto> buttonList;


}
