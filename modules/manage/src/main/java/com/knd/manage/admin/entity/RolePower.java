package com.knd.manage.admin.entity;

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
 * @since 2020-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("role_power")
@ApiModel(value="RolePower对象", description="")
public class RolePower extends BaseEntity {

    private static final long serialVersionUID=1L;

    private String roleId;

    private String powerId;


}
