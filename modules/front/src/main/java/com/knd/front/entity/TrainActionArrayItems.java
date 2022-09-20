package com.knd.front.entity;

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
 * @author llx
 * @since 2020-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("train_action_array_items")
@ApiModel(value="TrainActionArrayItems对象", description="")
public class TrainActionArrayItems extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "动作序列训练主表Id")
    private String trainActionArrayHeadId;


    @ApiModelProperty(value = "动作序列组序号")
    private String actionArraySetNum;



}
