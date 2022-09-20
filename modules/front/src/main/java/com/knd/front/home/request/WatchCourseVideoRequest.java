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
public class WatchCourseVideoRequest {
    @NotBlank(message = "用户id不能为空")
    private String userId;
    @NotBlank(message = "课程id不能为空")
    private String courseId;
    @NotBlank(message = "课程名称不能为空")
    private String course;
    @NotBlank(message = "设备编号不能为空")
    private String equipmentNo;
}