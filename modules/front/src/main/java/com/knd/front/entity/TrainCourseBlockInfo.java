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
 * @since 2020-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("train_course_block_info")
@ApiModel(value="TrainCourseBlockInfo对象", description="")
public class TrainCourseBlockInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "训练课程统计信息Id")
    private String trainCourseHeadInfoId;

    @ApiModelProperty(value = "训练block Id")
    private String blockId;

    @ApiModelProperty(value = "训练block名称")
    private String block;

    @ApiModelProperty(value = "训练block组序号")
    private String blockSetNum;

    @ApiModelProperty(value = "训练完成组数")
    private String sets;


}
