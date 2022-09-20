package com.knd.manage.mall.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class OrderInstallRequest {

    @ApiModelProperty(value = "用户类型 1客服2销售3技师4普通用户")
    private String roleId;

    @ApiModelProperty(value = "订单id")
    @NotBlank(message = "order_id不可空")
    @Size(max = 64, message = "order_id最大长度为64")
    private String tbOrderId;

    @ApiModelProperty(value = "安装位置")
    private String installPosition;

    @ApiModelProperty(value = "安装方式")
    private String mountType;

    @ApiModelProperty(value = "墙体结构")
    private String wallStructure;

    @ApiModelProperty(value = "墙面材质")
    private String wallMaterial;

    @ApiModelProperty(value = "地面材质")
    private String floorMaterial;

    @ApiModelProperty(value = "电源距离")
    private String powerDistance;

    @ApiModelProperty(value = "是否有电梯0是，1否")
    private String isElevator;

    @ApiModelProperty(value = "是否布线0是，1否")
    private String isWiring;

    @ApiModelProperty(value = "布线要求")
    private String wiringRequire;

//    @ApiModelProperty(value = "安装地址")
//    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "安装开始时间")
    private LocalDateTime installationBeginTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "安装结束时间")
    private LocalDateTime installationEndTime;

    @ApiModelProperty(value = "安装区域id")
    private String areaId;

    @ApiModelProperty(value = "安装人员id")
    private String personId;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "区")
    private String region;

    @ApiModelProperty(value = "街道")
    private String detailAddress;

    @ApiModelProperty(value = "门牌号")
    private String roomNo;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户电话")
    private String userPhone;


//    @NotBlank
//    @Pattern(regexp = "^(1|2)$")
//    private String postType;

//    private String icId;
}
