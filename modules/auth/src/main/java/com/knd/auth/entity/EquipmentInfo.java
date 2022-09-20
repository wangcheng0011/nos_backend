package com.knd.auth.entity;

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
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("equipment_info")
@ApiModel(value="EquipmentInfo对象", description="")
public class EquipmentInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "设备编号")
    private String equipmentNo;

    @ApiModelProperty(value = "激活状态")
    private String status;

    @ApiModelProperty(value = "描述")
    private String remark;

}
