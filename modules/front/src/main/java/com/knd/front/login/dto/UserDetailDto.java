package com.knd.front.login.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/20
 * @Version 1.0
 */
@Data
public class UserDetailDto {
    private String mobile;
    private String nickName;
    private String gender;
    private String birthDay;
    private String height;
    private String weight;
    private String bmi;
    private String trainHisFlag;
    private String target;
    private String targetId;
    private String shapeId;
    private String shape;
    private List<String> hobbyId;
    private String perSign;
    private String masterId;
    private String vipStatus;
    private LocalDate vipBeginDate;
    private LocalDate vipEndDate;
    private String headPicUrl;
    private String headPicUrlId;
    private String sportId;
    private String frequencyId;
}