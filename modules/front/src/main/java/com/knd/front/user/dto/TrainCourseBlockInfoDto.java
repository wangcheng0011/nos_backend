package com.knd.front.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/3
 * @Version 1.0
 */
@Data
public class TrainCourseBlockInfoDto {
    private String id;
    private String blockId;
    private String block;
    private String blockSetNum;
    private String sets;
    private String sort;
    private List<TrainCourseActInfoDto> actionList;


}