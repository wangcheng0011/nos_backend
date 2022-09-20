package com.knd.manage.mall.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 会员收货地址
 * 
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-08-06 16:14:34
 */
@Data
@ApiModel(value="用户收货地址请求对象", description="用户收货地址请求对象")
public class UserReceiveAddressRequest{

	/**
	 * 收货人姓名
	 */
	@ApiModelProperty(value = "id")
	private String id;
	/**

	/**
	 * 收货人姓名
	 */
	@ApiModelProperty(value = "收货人姓名")
	@NotBlank(message = "收货人姓名不能为空")
	private String name;
	/**
	 * 电话
	 */
	@ApiModelProperty(value = "手机号")
	@NotBlank(message = "手机号不能为空")
	@Pattern(
			regexp = "1(([38]\\d)|(5[^4&&\\d])|(4[579])|(7[0135678]))\\d{8}",
			message = "手机号格式不合法"
	)
	private String phone;
	/**
	 * 邮政编码
	 */
	@ApiModelProperty(value = "邮政编码")
	private String postCode;

	/**
	 * 邮政编码
	 */
	@ApiModelProperty(value = "userId")
	private String userId;

	/**
	 * 省份/直辖市
	 */
	@ApiModelProperty(value = "省份/直辖市")
	private String province;
	/**
	 * 城市
	 */
	@ApiModelProperty(value = "城市")
	private String city;
	/**
	 * 区
	 */
	@ApiModelProperty(value = "区")
	private String region;
	/**
	 * 详细地址(街道)
	 */
	@ApiModelProperty(value = "详细地址(街道)")
	private String detailAddress;
	/**
	 * 省市区代码
	 */
	@ApiModelProperty(value = "省市区代码")
	private String areacode;

	/**
	 * 经度
	 */
	@ApiModelProperty(value = "经度")
	private String longitude;

	/**
	 * 纬度
	 */
	@ApiModelProperty(value = "纬度")
	private String latitude;

	@ApiModelProperty(value = "门牌号")
	private String roomNo;

	@ApiModelProperty(value = "是否默认")
	private String defaultStatus;

}
