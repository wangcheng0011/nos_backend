package com.knd.auth.entity;

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
 * @since 2020-09-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="User对象", description="")
public class User extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "注册时间")
    private String registTime;

    @ApiModelProperty(value = "当前训练周期等级")
    private String trainLevel;

    @ApiModelProperty(value = "当前训练周期开始日期")
    private String trainPeriodBeginTime;

    @ApiModelProperty(value = "冻结标识")
    private String frozenFlag;


}
