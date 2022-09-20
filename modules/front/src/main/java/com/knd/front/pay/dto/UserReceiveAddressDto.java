package com.knd.front.pay.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 会员收货地址
 * 
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-08-06 16:14:34
 */
@Data
@ApiModel(value="UserReceiveAddressDto", description="")
public class UserReceiveAddressDto {

	/**
	 * 收货人姓名
	 */
	private String name;
	/**
	 * 电话
	 */
	private String phone;
	/**
	 * 邮政编码
	 */
	private String postCode;
	/**
	 * 省份/直辖市
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 区
	 */
	private String region;
	/**
	 * 详细地址(街道)
	 */
	private String detailAddress;
	/**
	 * 省市区代码
	 */
	private String areacode;

}
