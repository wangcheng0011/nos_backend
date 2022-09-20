package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("verify_code")
@ApiModel(value="VerifyCode对象", description="")
public class VerifyCode extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "验证码分类")
    private String codeType;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "失效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireTime;


}
