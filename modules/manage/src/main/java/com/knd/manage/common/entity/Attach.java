package com.knd.manage.common.entity;

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
 * @since 2020-07-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Attach对象", description="")
public class Attach extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "附件名称")
    private String fileName;

    @ApiModelProperty(value = "附件路径")
    private String filePath;

    @ApiModelProperty(value = "附件大小")
    private String fileSize;

    @ApiModelProperty(value = "附件后缀")
    private String fileType;


}
