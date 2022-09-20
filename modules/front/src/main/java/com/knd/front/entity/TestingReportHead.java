package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

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
@TableName("testing_report_head")
@ApiModel(value="TestingReportHead", description="")
public class TestingReportHead extends BaseEntity {

    private static final long serialVersionUID=1L;

    private String equipmentNo;
    private LocalDateTime reportTime;
    private String repairmanId;
    private String attachId;


}
