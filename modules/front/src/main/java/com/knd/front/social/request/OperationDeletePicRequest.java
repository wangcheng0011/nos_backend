package com.knd.front.social.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 删除照片
 */
@Data
public class OperationDeletePicRequest {

    @ApiModelProperty(value = "照片id集合")
    private List<String> picIdList;
}
