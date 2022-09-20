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
 * @since 2020-07-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_level_info")
@ApiModel(value="UserLevelInfo对象", description="")
public class UserLevelInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "会员Id")
    private String userId;

    @ApiModelProperty(value = "周序号")
    private String sort;

    @ApiModelProperty(value = "周开始日期")
    private String beginDate;

    @ApiModelProperty(value = "周结束日期")
    private String endDate;

    @ApiModelProperty(value = "当周星星情况")
    private String starFlag;

    @ApiModelProperty(value = "本周训练天数")
    private String trainDayCount;

    @ApiModelProperty(value = "缓冲天数")
    private String bufferDayCount;

    @ApiModelProperty(value = "当前训练周期id")
    private String userTrainLevelId;


}
