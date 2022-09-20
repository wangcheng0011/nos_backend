package com.knd.front.train.dto;

import lombok.Data;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/10
 * @Version 1.0
 */
@Data
public class CourseVideoProgressInfoDto {
    private String progressName;
    private String progressStartSeconds;
    private String progressEndSeconds;
    private String nodeSort;
    private String aimSetNum;
}