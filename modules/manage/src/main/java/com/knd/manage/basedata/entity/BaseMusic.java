package com.knd.manage.basedata.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zm
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_music")
@ApiModel(value="BaseMusic对象", description="")
public class BaseMusic extends BaseEntity {
    private static final long serialVersionUID=1L;
    private String type;
    private String name;
    private String remark;

    private String musicUrlId;

}
