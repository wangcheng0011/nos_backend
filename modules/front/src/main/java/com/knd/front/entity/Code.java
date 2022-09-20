package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Code对象", description="")
public class Code extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "大分类Code")
    @TableField("bigClassCode")
    private String bigClassCode;

    @ApiModelProperty(value = "小分类Code")
    @TableField("smallClassCode")
    private String smallClassCode;

    @ApiModelProperty(value = "小分类名称")
    @TableField("smallClassName")
    private String smallClassName;

    @ApiModelProperty(value = "描述")
    private String remark;




}
