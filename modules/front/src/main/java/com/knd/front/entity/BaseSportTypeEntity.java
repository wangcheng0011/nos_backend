package com.knd.front.entity;

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
@TableName("base_sport_type")
@ApiModel(value="BaseSportTypeEntity对象", description="")
public class BaseSportTypeEntity extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "运动方式")
    private String type;

    @ApiModelProperty(value = "描述")
    private String remark;


}
