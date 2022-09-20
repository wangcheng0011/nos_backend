package com.knd.manage.course.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;

@Data
public class Block {
    private String id;
    @ApiParam("block名称")
    private String block;
    @ApiParam("目标组数")
    private String aimSetNum;
    @ApiParam("排序号")
    private String sort;
//    @ApiParam("视频小节列表")
//    private List<Node> nodes;
}
