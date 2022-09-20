package com.knd.manage.basedata.dto;

import com.knd.manage.basedata.entity.BaseBodyPart;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PartListDto {
    //总数
    private int total;
    //列表
    private List<BaseBodyPart> partList;
}
