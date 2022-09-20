package com.knd.manage.course.vo;


import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BlockSort {

    @NotBlank
    @ApiParam("blockId")
    private String id;
    @NotBlank
    @ApiParam("排序号")
    private String sort;

}
