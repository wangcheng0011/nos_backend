package com.knd.manage.basedata.entity;

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
 * @author zm
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_frequency")
@ApiModel(value="BaseFrequencyEntity对象", description="")
public class BaseFrequencyEntity extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "运动频率")
    private String frequency;

    @ApiModelProperty(value = "描述")
    private String remark;


}
