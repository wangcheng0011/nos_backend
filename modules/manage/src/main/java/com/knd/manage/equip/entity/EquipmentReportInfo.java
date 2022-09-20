package com.knd.manage.equip.entity;

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
 * @author sy
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("equipment_report_info")
@ApiModel(value="EquipmentReportInfo对象", description="")
public class EquipmentReportInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

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

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人员")
    private String createUser;



}
