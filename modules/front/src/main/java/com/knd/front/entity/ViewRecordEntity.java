package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


/**
 * @author will
 */
@Data
@EqualsAndHashCode
@ApiModel(value="浏览记录", description="")
public class ViewRecordEntity {

    private static final long serialVersionUID=1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    protected String id;

    @ApiModelProperty(value = "会员ID")
    private String userId;

    @ApiModelProperty(value = "项目类型")
    private Integer type;

    @ApiModelProperty(value = "项目Id")
    private String itemId;

    @ApiModelProperty(value = "作成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createDate;

}
