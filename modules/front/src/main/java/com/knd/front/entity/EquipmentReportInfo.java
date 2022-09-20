package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author llx
 * @since 2020-07-08
 */
@Data
@TableName("equipment_report_info")
@ApiModel(value="EquipmentReportInfo对象", description="")
public class EquipmentReportInfo  {

    private static final long serialVersionUID=1L;
    private String id;
    @ApiModelProperty(value = "设备编号")
    private String equipmentNo;

    @ApiModelProperty(value = "开机时间")
    private String turnOnTime;

    @ApiModelProperty(value = "关机时间")
    private String turnOffTime;

    @ApiModelProperty(value = "硬件版本号")
    private String hardVersion;

    @ApiModelProperty(value = "下控版本号")
    private String mainboardVersion;

    @ApiModelProperty(value = "app版本号")
    private String appVersion;

    @ApiModelProperty(value = "位置信息")
    private String positionInfo;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人员")
    private String createUser;
    private String deleted;

}
