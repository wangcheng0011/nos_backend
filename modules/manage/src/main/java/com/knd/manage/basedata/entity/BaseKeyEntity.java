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
@TableName("base_key")
@ApiModel(value="BaseKeyEntity对象", description="")
public class BaseKeyEntity extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "key")
    private String keyValue;

    @ApiModelProperty(value = "描述")
    private String remark;


}
