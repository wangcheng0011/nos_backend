package com.knd.manage.basedata.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_area")
@ApiModel(value="BaseArea对象", description="")
public class BaseArea extends BaseEntity {
    private static final long serialVersionUID=1L;
    private String area;

}
