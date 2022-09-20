package com.knd.auth.entity;

import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
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
@ApiModel(value="Role对象", description="")
public class Role extends BaseEntity {

    private static final long serialVersionUID=1L;

    private String name;

    private String memo;


}
