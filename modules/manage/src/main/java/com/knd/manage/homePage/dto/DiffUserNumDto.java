package com.knd.manage.homePage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zm
 */
@Data
public class DiffUserNumDto {

    @ApiModelProperty(value = "付费用户总数")
    private String payingUser;

    @ApiModelProperty(value = "付费用户月增长")
    private String payingUserGrowth;

    @ApiModelProperty(value = "续费用户总数")
    private String renewalUser;

    @ApiModelProperty(value = "续费用户月增长")
    private String renewalUserGrowth;

    @ApiModelProperty(value = "流失用户总数")
    private String lostUsers;

    @ApiModelProperty(value = "流失月增长")
    private String lostUsersGrowth;

    @ApiModelProperty(value = "注册教练总数")
    private String registeredCoach;

    @ApiModelProperty(value = "注册教练月增长")
    private String registeredCoachGrowth;

}
