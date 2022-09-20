package com.knd.front.social.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员收货地址
 * 
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-08-06 16:14:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_social_moment_address")
@ApiModel(value="UserSocialMomentAddressEntity对象", description="")
public class UserSocialMomentAddressEntity extends BaseEntity {

	private static final long serialVersionUID=1L;
	/**
	 * 动态Id
	 */
	private String momentId;
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
	 * 详细地址
	 */
	private String roomNo;
	/**
	 * 省市区代码
	 */
	private String areacode;

	/**
	 * 经度
	 */
	private String longitude;

	/**
	 * 纬度
	 */
	private String latitude;



}
