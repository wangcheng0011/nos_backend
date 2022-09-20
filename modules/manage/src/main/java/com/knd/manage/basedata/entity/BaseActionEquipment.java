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
@TableName("base_action_equipment")
@ApiModel(value="BaseActionEquipment对象", description="")
public class BaseActionEquipment extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "动作id")
    private String actionId;

    @ApiModelProperty(value = "适宜器材id")
    private String equipmentId;


}
