package com.knd.manage.equip.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author sy
 * @since 2020-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_release_version")
@ApiModel(value="AppReleaseVersion对象", description="")
public class AppReleaseVersion extends BaseEntity {

    private static final long serialVersionUID=1L;
    @ApiModelProperty(value = "版本号前缀")
    private String versionPrefix;
    @ApiModelProperty(value = "版本号")
    private String appVersion;

    @ApiModelProperty(value = "apk附件id")
    private String attachId;

    @ApiModelProperty(value = "发布状态")
    private String releaseStatus;

    @ApiModelProperty(value = "app类型")
    private String appType;

    @ApiModelProperty(value = "是否强制更新")
    private String forceFlag;

    @ApiModelProperty(value = "发布内容")
    private String content;

    @ApiModelProperty(value = "发布时间")
    private String releaseTime;



}
