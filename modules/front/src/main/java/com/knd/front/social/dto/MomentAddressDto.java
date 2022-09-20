package com.knd.front.social.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 动态地址
 * 
 * @author zm
 */
@Data
@ApiModel(value="动态地址", description="动态地址")
public class MomentAddressDto {

	@ApiModelProperty(value = "省份/直辖市")
	private String province;

	@ApiModelProperty(value = "市")
	private String city;

	@ApiModelProperty(value = "区")
	private String region;

	@ApiModelProperty(value = "街道")
	private String detailAddress;

	@ApiModelProperty(value = "门牌号")
	private String roomNo;

	@ApiModelProperty(value = "省市区代码")
	private String areacode;

	@ApiModelProperty(value = "经度")
	private String longitude;

	@ApiModelProperty(value = "纬度")
	private String latitude;


}
