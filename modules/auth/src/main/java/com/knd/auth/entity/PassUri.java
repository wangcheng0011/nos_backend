package com.knd.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2020-07-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pass_uri")
@ApiModel(value="PassUri对象", description="")
public class PassUri extends BaseEntity {

    private static final long serialVersionUID=1L;

    private String uri;

    @ApiModelProperty(value = "方法")
    private String method;

    @ApiModelProperty(value = "1: 未登录可访问 2: 需登录可访问")
    private int type;


}
