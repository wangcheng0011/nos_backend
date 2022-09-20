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
@TableName("base_body_part")
@ApiModel(value="BaseBodyPart对象", description="")
public class BaseBodyPart extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "部位")
    private String part;

    @ApiModelProperty(value = "描述")
    private String remark;


}
