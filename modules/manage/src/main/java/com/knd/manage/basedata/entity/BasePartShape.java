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
@TableName("base_part_shape")
@ApiModel(value="BasePartShape对象", description="")
public class BasePartShape extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "体型")
    private String shape;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "部位id")
    private String partId;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "选中图片")
    private String selectUrlId;

    @ApiModelProperty(value = "未选中图片")
    private String unSelectUrlId;

}
