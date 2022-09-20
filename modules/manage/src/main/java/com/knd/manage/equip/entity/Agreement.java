package com.knd.manage.equip.entity;

import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author will
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="协议", description="")
public class Agreement extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "协议名称")
    private String agreementName;

    @ApiModelProperty(value = "协议内容")
    private String agreementContent;

}
