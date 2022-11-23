package com.knd.manage.wallpaper.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author wangcheng
 */
@Data
@TableName("wallPaper_attach")
public class WallPaperAttachEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "排序")
	private String sort;

	@ApiModelProperty(value = "是否选中 0 未选中 1 选中")
	private String selected;

	@ApiModelProperty(value = "图片Id")
	private String attachUrlId;

	@ApiModelProperty(value = "排序 1系统壁纸 2自定义壁纸")
	private String type;

	@ApiModelProperty(value = "壁纸名称")
	private String wallpaperName;

	@ApiModelProperty(value = "用户id")
	private String userId;


}
