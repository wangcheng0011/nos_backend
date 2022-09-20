package com.knd.manage.basedata.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zm
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_page_floor")
@ApiModel(value="BasePageFloor对象", description="")
public class BasePageFloor extends BaseEntity {

    private static final long serialVersionUID=1L;

    private String pageId;

    private String floorId;

    private String sort;

}
