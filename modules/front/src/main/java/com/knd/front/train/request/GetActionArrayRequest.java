package com.knd.front.train.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.LinkedList;

/**
 * @author will
 */
@Data
public class GetActionArrayRequest {
    @ApiModelProperty(value = "用户id")
//    @NotBlank(message = "用户id不能为空")
    private String userId;
    @ApiModelProperty(value = "当前请求页码")
    @NotBlank(message = "当前请求页码不能为空")
    private String currentPage;
    @ApiModelProperty(value = "actionQuantity或createDate")
    @Pattern(regexp = "^(actionQuantity|createDate)$")
    @NotBlank(message = "只能是actionQuantity或createDate")
    private String sortField;
    @ApiModelProperty(value = "升序ASC 降序DESC")
    @Pattern(regexp = "^(ASC|DESC)$")
    @NotBlank(message = "升序ASC 降序DESC")
    private String sort;
}