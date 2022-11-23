package com.knd.front.wallpaper.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author wangcheng
 */
@Data
@TableName("user_wallpaper")
public class UserWallPaperEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "壁纸Id")
	private String wallpaperId;

	@ApiModelProperty(value = "用户id")
	private String userId;


}
