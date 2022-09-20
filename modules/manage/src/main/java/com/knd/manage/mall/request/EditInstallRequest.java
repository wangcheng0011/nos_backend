package com.knd.manage.mall.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.common.em.InstallOperationEnum;
import com.knd.manage.basedata.vo.VoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EditInstallRequest {

    @ApiModelProperty(value = "订单id")
    @NotBlank(message = "order_id不可空")
    @Size(max = 64, message = "order_id最大长度为64")
    private String tbOrderId;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "安装开始时间")
    private LocalDateTime installationBeginTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "安装结束时间")
    private LocalDateTime installationEndTime;

    @ApiModelProperty(value = "安装状态")
    private InstallOperationEnum flag;

    @ApiModelProperty(value = "签字图片")
    private VoUrl signatureUrl;

    @ApiModelProperty(value = "安装图片")
    private List<VoUrl> installUrl;

    @ApiModelProperty(value = "安装人员id")
    @NotBlank(message = "安装人员id不可空")
    @Size(max = 64, message = "安装人员id最大长度为64")
    private String personId;

    @ApiModelProperty(value = "签到信息")
    private UserLocationAddressRequest addressRequest;
}
