package com.knd.manage.user.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

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
@TableName("user")
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
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String frozenFlag;

    @ApiModelProperty(value = "会员类型 0普通会员 1个人会员 2家庭会员-主 3家庭会员-副")
    private String vipStatus;

    @ApiModelProperty(value = "会员开始时间")
    private LocalDate vipBeginDate;

    @ApiModelProperty(value = "会员结束时间")
    private LocalDate vipEndDate;

    @ApiModelProperty(value = "器械Id")
    private String sid;

    @ApiModelProperty(value = "苹果id")
    private String appleId;

    @ApiModelProperty(value = "苹果昵称")
    private String appleNickname;

    @ApiModelProperty(value = "qq登陆id")
   // @TableField(fill = FieldFill.INSERT_UPDATE)
    private String qqOpenId;

    @ApiModelProperty(value = "qq昵称")
    private String qqNickname;

    @ApiModelProperty(value = "qq头像url")
    private String qqAvatar;

    @ApiModelProperty(value = "unionId")

    private String unionId;

    @ApiModelProperty(value = "微信OpenId")
   // @TableField(fill = FieldFill.INSERT_UPDATE)
    private String wxOpenId;

    @ApiModelProperty(value = "微信昵称")
    private String wxNickname;

    @ApiModelProperty(value = "微信头像url")
    private String wxAvatar;

    @ApiModelProperty(value = "推荐部位")
    private String recommendBodyPart;

    @ApiModelProperty(value = "小程序openId")
    private String smallRoutineOpenId;

    @ApiModelProperty(value = "小程序头像")
    private String smallRoutineHeadPic;


}
