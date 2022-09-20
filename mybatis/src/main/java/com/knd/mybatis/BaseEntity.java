package com.knd.mybatis;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


/**
 * Entity基类
 *
 * @author JinJichao
 */
@Data
public class BaseEntity<T extends Model> extends Model {

    private static final Long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    protected String id;

    @ApiModelProperty(value = "作成者")
    @TableField(fill = FieldFill.INSERT)
    protected String createBy;

    @ApiModelProperty(value = "作成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createDate;

    @ApiModelProperty(value = "更新者")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected String lastModifiedBy;


    @ApiModelProperty(value = "更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastModifiedDate;

    /**
     * 逻辑删除字段
     */
    @ApiModelProperty(value = "删除标志")
    @TableField(fill = FieldFill.INSERT)
    private String deleted;


}
