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
 * @author sy
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_target")
@ApiModel(value="BaseTarget对象", description="")
public class BaseTarget extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "目标")
    private String target;

    @ApiModelProperty(value = "描述")
    private String remark;


}
