package com.knd.manage.basedata.entity;

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
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_part_hobby")
@ApiModel(value="BasePartHobby对象", description="")
public class BasePartHobby extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "爱好")
    private String hobby;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "部位id")
    private String partId;

    @ApiModelProperty(value = "选中图片")
    private String selectUrlId;

    @ApiModelProperty(value = "未选中图片")
    private String unSelectUrlId;


}
