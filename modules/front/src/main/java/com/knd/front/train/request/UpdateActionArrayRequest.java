package com.knd.front.train.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.LinkedList;
/**
 * @author will
 */
@Data
public class UpdateActionArrayRequest {
    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;
    @ApiModelProperty(value = "动作组合名称")
    @NotBlank(message = "动作组合名称不能为空")
    private String actionArrayName;
    @ApiModelProperty(value = "共享状态 0私有1共享")
    @Pattern(regexp = "^(0|1)$")
    @NotBlank(message = "共享状态 0私有1共享")
    private String shareStatus;
    //@NotBlank(message = "排序")
    //private String sort;
//    @Valid
    private LinkedList<SaveActionArrayDetailRequest> saveActionArrayDetailRequests;
}