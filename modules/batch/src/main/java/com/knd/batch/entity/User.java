package com.knd.batch.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author sy
 * @since 2020-08-13
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String registTime;

    @ApiModelProperty(value = "当前训练周期等级")
    private String trainLevel;

    @ApiModelProperty(value = "当前训练周期开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String trainPeriodBeginTime;

    @ApiModelProperty(value = "冻结标识")
    private String frozenFlag;

    @ApiModelProperty(value = "主账号Id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String masterId;

    @ApiModelProperty(value = "会员类型 0普通会员 1个人会员 2家庭会员-主 3家庭会员-副")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String vipStatus;

    @ApiModelProperty(value = "会员开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "vipBeginDate", fill = FieldFill.INSERT_UPDATE)
    private LocalDate vipBeginDate;

    @ApiModelProperty(value = "会员结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "vipEndDate",fill = FieldFill.INSERT_UPDATE)
    private LocalDate vipEndDate;

    @ApiModelProperty(value = "器械Id")
    private String sid;

    @ApiModelProperty(value = "苹果Id")
    private String appleId;

    @ApiModelProperty(value = "苹果昵称")
    private String appleNickname;

    @ApiModelProperty(value = "qq登陆id")
    private String qqOpenId;

    @ApiModelProperty(value = "qq昵称")
    private String qqNickname;

    @ApiModelProperty(value = "qq头像url")
    private String qqAvatar;

    @ApiModelProperty(value = "微信登陆id")
    private String wxOpenId;

    @ApiModelProperty(value = "unionId")
    private String unionId;

    @ApiModelProperty(value = "微信昵称")
    private String wxNickname;

    @ApiModelProperty(value = "微信头像url")
    private String wxAvatar;


}
