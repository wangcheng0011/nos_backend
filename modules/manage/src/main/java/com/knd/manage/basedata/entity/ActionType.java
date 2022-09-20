package com.knd.manage.basedata.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("action_type")
@ApiModel(value="ActionType对象", description="")
public class ActionType extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "名称")
    private String name;


}
