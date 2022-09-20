package com.knd.manage.basedata.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_difficulty")
@ApiModel(value="BaseDifficulty对象", description="")
public class BaseDifficulty extends BaseEntity {
    private static final long serialVersionUID=1L;
    private String type;
    private String remark;
    private String difficulty;
    private String imageUrlId;

}
