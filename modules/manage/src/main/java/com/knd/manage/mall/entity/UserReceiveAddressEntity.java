package com.knd.manage.mall.entity;

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
@TableName("ums_member_receive_address")
@ApiModel(value="UserReceiveAddressEntity对象", description="")
public class
UserReceiveAddressEntity extends BaseEntity {

	private static final long serialVersionUID=1L;
	/**
	 * userId
	 */
	private String userId;
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
	 * 门牌号
	 */
	private String roomNo;
	/**
	 * 省市区代码
	 */
	private String areacode;
	/**
	 * 是否默认
	 */
	private String defaultStatus;
	/**
	 * 经度
	 */
	private String longitude;
	/**
	 * 纬度
	 */
	private String latitude;



}
