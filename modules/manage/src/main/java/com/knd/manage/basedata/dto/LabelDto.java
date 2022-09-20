package com.knd.manage.basedata.dto;

import com.knd.manage.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class LabelDto {
    private String id;
    private String type;
    private String label;
    private String remark;
    @ApiModelProperty(value = "图片")
    private ImgDto imageUrl;

    private List<UserModelDto> userList;
}
