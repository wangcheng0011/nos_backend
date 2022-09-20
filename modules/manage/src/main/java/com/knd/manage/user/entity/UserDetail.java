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
@TableName("user_detail")
@ApiModel(value="UserDetail对象", description="")
public class UserDetail extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "会员Id")
    private String userId;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "出生日期")
    private String birthDay;

    @ApiModelProperty(value = "身高")
    private String height;

    @ApiModelProperty(value = "体重")
    private String weight;

    @ApiModelProperty(value = "BMI")
    private String bmi;

    @ApiModelProperty(value = "运动频率id")
    private String frequencyId;

    @ApiModelProperty(value = "运动类型 id")
    private String sportTypeId;

    @ApiModelProperty(value = "健身基础Flag")
    private String trainHisFlag;

    @ApiModelProperty(value = "健身目标(Id）")
    private String targetId;

    @ApiModelProperty(value = "健身目标")
    private String target;

    @ApiModelProperty(value = "头像id")
    private String headPicUrlId;

    @ApiModelProperty(value = "体型id")
    private String shapeId;

    @ApiModelProperty(value = "爱好id")
    private String hobbyId;

    @ApiModelProperty(value = "签名")
    private String perSign;

    @ApiModelProperty(value = "运动方式id")
    private String sportId;



}
