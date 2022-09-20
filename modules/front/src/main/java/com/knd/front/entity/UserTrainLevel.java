package com.knd.front.entity;

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
 * @author llx
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_train_level")
@ApiModel(value="UserTrainLevel对象", description="")
public class UserTrainLevel extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "训练周期等级")
    private String trainLevel;

    @ApiModelProperty(value = "训练周期开始日期")
    private String trainPeriodBeginTime;

    @ApiModelProperty(value = "训练周期结束日期")
    private String trainPeriodEndTime;


}
