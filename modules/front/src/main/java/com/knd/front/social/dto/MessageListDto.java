package com.knd.front.social.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @我列表信息
 */
@Data
public class MessageListDto {

    @ApiModelProperty(value = "类型0动态回复 1评论回复 2@ 3点赞")
    private String type;

    @ApiModelProperty(value = "动态id")
    private String momentId;

    @ApiModelProperty(value = "动态标题")
    private String title;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "头像url")
    private String userHeadPicUrl;

    @ApiModelProperty(value = "评论或者@的内容")
    private String content;

    @ApiModelProperty(value = "时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;

}
