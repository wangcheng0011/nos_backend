package com.knd.manage.course.vo;

import com.knd.manage.equip.entity.CourseEquipment;
import com.knd.manage.equip.entity.EquipmentInfo;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class VoSaveCourseHead {
    //userId从token获取
    private String userId;
    @NotBlank(message = "课程名称不可空")
    @Size(max = 32, message = "最大长度为32")
    @ApiParam("课程名称")
    private String course;
    @NotBlank(message = "训练课程标识不可空")
    @Size(max = 1, message = "最大长度为1")
    @Pattern(regexp = "^(0|1)$", message = "训练课程标识错误")
    @ApiParam("训练课程标识")
    private String trainingFlag;
    @NotBlank(message = "视频介绍不可空")
    @Size(max = 256, message = "最大长度为256")
    @ApiParam("视频介绍")
    private String remark;
    @Size(max = 256, message = "最大长度为256")
    @ApiParam("适用人群介绍")
    private String fitCrowdRemark;
    @Size(max = 256, message = "最大长度为256")
    @ApiParam("禁忌人群介绍")
    private String unFitCrowdRemark;
    @NotBlank(message = "操作类型不可空")
    @Size(max = 1, message = "最大长度为1")
    @Pattern(regexp = "^(1|2)$", message = "操作类型错误")
    @ApiParam("操作类型")
    private String postType;
    @Size(max = 64, message = "最大长度为64")
    @ApiParam("课程id，更新时必传")
    private String id;
    @NotEmpty(message = "分类标签列表不可空")
    @ApiParam("分类标签列表")
    private List<VoType> typeList;
    @ApiParam("设备信息列表")
    private List<CourseEquipment> equipmentList;
    @ApiParam("目标列表")
    private List<VoTarget> targetList;
    @ApiParam("部位标签列表")
    private List<VoPart> partList;
    @NotBlank(message = "是否大屏显示不可空")
    @Size(max = 1, message = "最大长度为1")
    @Pattern(regexp = "^(0|1)$", message = "设备首页显示参数错误")
    @ApiParam("是否设备首页显示")
    private String appHomeFlag;
    @NotBlank(message = "适用会员范围不可空")
    @Size(max = 1, message = "最大长度为1")
    @Pattern(regexp = "^(0|1)$", message = "适用会员范围参数错误")
    @ApiParam("适用会员范围")
    private String userScope;
    @NotBlank(message = "显示顺序不可空")
    @Size(max = 8, message = "最大长度为8")
    @ApiParam("显示顺序")
    private String sort;
    @NotBlank(message = "频率不可空")
    @Size(max = 64, message = "最大长度为8")
    @ApiParam("显示频率")
    private String frequency;
    @NotBlank(message = "介绍视频原名称不可空")
    @Size(max = 256, message = "最大长度为256")
    //介绍视频原名称
    private String videoAttachName;
    @NotBlank(message = "介绍视频新名称不可空")
    @Size(max = 256, message = "最大长度为256")
    //介绍视频新名称
    private String videoAttachNewName;
    @NotBlank(message = "介绍视频大小不可空")
    @Size(max = 32, message = "最大长度为32")
    //介绍视频大小
    private String videoAttachSize;
    @NotBlank(message = "封面图片原名称不可空")
    @Size(max = 256, message = "最大长度为256")
    //封面图片原名称
    private String picAttachName;
    @NotBlank(message = "封面图片新名称不可空")
    @Size(max = 256, message = "最大长度为256")
    //封面图片新名称
    private String picAttachNewName;
    @NotBlank(message = "封面图片大小不可空")
    @Size(max = 32, message = "最大长度为32")
    //封面图片大小
    private String picAttachSize;

    @NotBlank
    @Size(max = 10, message = "最大长度为1")
    private String courseType;

    @NotBlank
    private String amount;

    @NotBlank
    private String difficultyId;



}

