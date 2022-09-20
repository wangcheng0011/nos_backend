package com.knd.manage.admin.entity;

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
 * @since 2020-07-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Power对象", description="")
public class Power extends BaseEntity {

    private static final long serialVersionUID=1L;

    private String moduleId;

    private String moduleName;

    private String pageId;

    private String pageName;

    private String buttonId;

    private String buttonName;

    private String powerFlag;



}
