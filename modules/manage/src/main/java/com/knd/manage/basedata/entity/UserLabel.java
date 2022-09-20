package com.knd.manage.basedata.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_label")
@ApiModel(value="UserLabel对象", description="")
public class UserLabel extends BaseEntity {
    private static final long serialVersionUID=1L;
    private String userId;
    private String labelId;
}
