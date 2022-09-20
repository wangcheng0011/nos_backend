package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author will
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("refresh_password_record")
@ApiModel(value="RefreshPasswordRecord对象", description="")
public class RefreshPasswordRecord extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "验证码")
    private String code ;

}
