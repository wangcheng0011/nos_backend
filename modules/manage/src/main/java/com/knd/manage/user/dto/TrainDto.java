package com.knd.manage.user.dto;

import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class TrainDto {
    @ApiParam("会员id")
    private String id;
    @ApiParam("训练Id")
    private String trainReportId;
    @ApiParam("昵称")
    private String nickName;
    @ApiParam("手机号")
    private String mobile;
    @ApiParam("训练类型")
    private String trainType;
    @ApiParam("训练课名称")
    private String trainName;
    @ApiParam("训练时间开始")
    private String trainBeginTime;
    @ApiParam("训练时间结束")
    private String trainEndTime;
    @ApiParam("设备编号")
    private String equipmentNo;


}
