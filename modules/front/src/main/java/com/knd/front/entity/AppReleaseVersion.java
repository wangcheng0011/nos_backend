package com.knd.front.entity;

import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AppReleaseVersion对象", description="")
public class AppReleaseVersion extends BaseEntity {

    private static final long serialVersionUID=1L;

    private String appVersion;
    private String attachId;
    private String releaseStatus;
    private String forceFlag;
    private String content;


}
