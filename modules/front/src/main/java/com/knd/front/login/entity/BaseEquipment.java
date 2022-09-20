package com.knd.front.login.entity;

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
@TableName("base_equipment")
@ApiModel(value="BaseEquipment对象", description="")
public class BaseEquipment extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "器材名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String remark;


}
