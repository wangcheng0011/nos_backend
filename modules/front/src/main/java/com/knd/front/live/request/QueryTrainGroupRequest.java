package com.knd.front.live.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class QueryTrainGroupRequest {

    @ApiModelProperty(value = "查询分类：1->推荐小组 2->我的小组 3->全部小组")
    @Pattern(regexp = "^(1|2|3)$",message = "查询分类只能是1->推荐小组 2->我的小组 3->全部小组")
    private String queryType;


    @ApiModelProperty(value = "查询关键字")
    private String keyword;

    @NotBlank(message = "当前页不能为空")
    @ApiModelProperty(value = "当前页" ,required = true)
    private String currentPage;

}
