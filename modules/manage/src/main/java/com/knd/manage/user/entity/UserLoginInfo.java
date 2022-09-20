package com.knd.manage.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author sy
 * @since 2020-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_login_info")
@ApiModel(value="UserLoginInfo对象", description="")
public class UserLoginInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "会员Id")
    private String userId;

    @ApiModelProperty(value = "登录时间")
    private String loginInTime;

    @ApiModelProperty(value = "登录设备编号")
    private String equipmentNo;

    @ApiModelProperty(value = "退出时间")
    private String loginOutTime;


}
