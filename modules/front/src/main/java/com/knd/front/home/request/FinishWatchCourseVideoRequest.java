package com.knd.front.home.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Data
public class FinishWatchCourseVideoRequest {
    @NotBlank(message = "用户id不能为空")
    private String userId;
    @NotBlank(message = "课程Id不能为空")
    private String introductionCourseId;
}