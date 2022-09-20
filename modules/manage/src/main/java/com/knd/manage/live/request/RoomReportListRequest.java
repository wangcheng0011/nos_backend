package com.knd.manage.live.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class RoomReportListRequest {

  /*  @Size(max = 64)
    private String roomId;

    @Size(max = 64)
    private String reportUserId;*/

    @Size(max = 64)
    @ApiModelProperty(value = "举报人昵称")
    private String nickName;

    @Size(max = 64)
    @ApiModelProperty(value = "举报人手机号")
    private String mobile;

    private String current;

    @ApiModelProperty(value = "举报类型")
    @Pattern(regexp = "^(0|1|2)$", message = "举报类型只能是0->小组房间1->直播课2->私教课")
    @NotEmpty(message = "举报类型必传")
    private String type;
}