package com.knd.front.home.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UserQueryCourseRequest {

    @ApiModelProperty(value = "课程类型0普通课程 1特色课程")
    private String courseType;

    @ApiModelProperty(value = "难度id：1基础 2初级 3中级 4高级")
    private String difficultyId;

    @ApiModelProperty(value = "部位id")
    private List<String> partIdList;

    @ApiModelProperty(value = "目标id")
    private List<String> targetIdList;

    @ApiModelProperty(value = "爱好id")
    private List<String> hobbyIdList;

    @ApiModelProperty(value = "排序内容videoDurationMinutes/amount/createDate  新课优先 根据sortContent=createDate 和sortOrder=DESC筛选")
    private String sortContent;

    @ApiModelProperty(value = "排序方式ASC/DESC")
    private String sortOrder;

    @NotBlank(message = "当前页不能为空")
    @ApiModelProperty(value = "当前页" ,required = true)
    private String currentPage;

}
