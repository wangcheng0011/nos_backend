package com.knd.manage.course.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.manage.basedata.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @author zm
 * 教练列表
 */
@Data
public class CoachListDto {

    @ApiModelProperty(value = "教练主键id")
    private String id;

    @ApiModelProperty(value = "教练用户id")
    private String userId;

    @ApiModelProperty(value = "姓名")
    private String nickName;

    @ApiModelProperty(value = "电话")
    private String mobile;

    @ApiModelProperty(value = "图片")
    private List<ImgDto> imageUrl;

    @ApiModelProperty(value = "注册时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String registTime;
}
