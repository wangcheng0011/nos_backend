package com.knd.front.train.dto;

import com.knd.front.home.dto.BaseBodyPartDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Data
public class CourseDetailDto {
    private String courseId;
    private String course;
    private String trainingFlag;
    private String remark;
    private String fitCrowdRemark;
    private String unFitCrowdRemark;
    private String videoAttachUrl;
    private String videoSize;
    private String picAttachUrl;
    private List<BaseBodyPartDto> typeList;
    private List<BaseBodyPartDto> targetList;
    private List<BaseBodyPartDto> partList;
    private List<BaseEquipmentDto> equipmentList;
    private String videoDurationMinutes;
    private String videoDurationSeconds;
    private List<BlockDto> blockList;
    @ApiModelProperty(value = "购买状态：1可观看  2购买VIP  3购买商品")
    private String buyStatus;
    private String frequency;
    private String appHomeFlag;
    private String releaseFlag;
}