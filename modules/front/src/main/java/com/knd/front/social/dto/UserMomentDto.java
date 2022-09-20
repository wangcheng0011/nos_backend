package com.knd.front.social.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.common.em.FollowStatusEnum;
import com.knd.front.pay.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 动态
 */
@Data
public class UserMomentDto {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户头像url")
    private String userHeadUrl;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "正文")
    private String content;

    @ApiModelProperty(value = "照片")
    private List<ImgDto> imageUrl;

    @ApiModelProperty(value = "发表日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "点赞数")
    private long thumbup;

    @ApiModelProperty(value = "评论数")
    private long comments;

    @ApiModelProperty(value = "是否点赞：0否 1是")
    private String isPraise;

    @ApiModelProperty(value = "评论信息")
    private List<MomentCommentDto> momentCommentDtoList;

}
