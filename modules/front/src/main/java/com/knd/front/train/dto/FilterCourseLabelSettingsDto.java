package com.knd.front.train.dto;

import com.knd.front.home.dto.*;
import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/9
 * @Version 1.0
 */
@Data
public class FilterCourseLabelSettingsDto {
    private List<BaseCourseTypeDto> typeList;
    private List<BaseTargetDto> targetList;
    private List<BaseBodyPartDto> partList;
    private List<BaseDifficultyDto> difficultyList;
    private List<BaseLabelDto> labelList;
}