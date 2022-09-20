package com.knd.front.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@Data
public class OrderUser {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "头像")
    private String headPicUrl;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "会员类型 0普通会员 1个人会员 2家庭会员-主 3家庭会员-副")
    private String vipStatus;


}
