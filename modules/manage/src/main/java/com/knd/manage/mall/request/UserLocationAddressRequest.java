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
@ApiModel(value="打卡地址对象", description="打卡地址")
public class UserLocationAddressRequest {

	/**
	 * 签到状态
	 */
	@ApiModelProperty(value = "签到状态")
	private String locationStatus;

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

}
