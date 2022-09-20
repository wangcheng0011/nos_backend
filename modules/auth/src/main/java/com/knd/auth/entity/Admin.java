package com.knd.auth.entity;

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
 * @since 2020-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Admin对象", description="")
public class Admin extends BaseEntity {

    private static final long serialVersionUID=1L;

    private String userName;

    private String password;

    private String salt;

    private String nickName;

    private String mobile;

    private String frozenFlag;


}
