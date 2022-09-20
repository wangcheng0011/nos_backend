package com.knd.manage.user.vo;

import com.knd.manage.basedata.vo.VoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VoSaveUserWxInfo {
    private String userId;
    private String wxNickname;
    @ApiModelProperty(value = "头像图片")
    private VoUrl headAttachUrl;

}
