package com.knd.front.help.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.front.pay.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HelpDto {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "图片")
    private List<ImgDto> imageUrl;

    //介绍视频Url
    private String videoAttachUrl;
    //介绍视频原名称
    private String videoAttachName;
    //介绍视频新名称
    private String videoAttachNewName;
    //介绍视频大小
    private String videoAttachSize;

    @ApiModelProperty(value = "作成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createDate;





}
