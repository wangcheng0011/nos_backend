package com.knd.manage.basedata.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_label")
@ApiModel(value="BaseLabel对象", description="")
public class BaseLabel extends BaseEntity {
    private static final long serialVersionUID=1L;
    private String type;
    private String label;
    private String remark;
    private String imageUrlId;
}
