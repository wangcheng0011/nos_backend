package com.knd.front.food.vo;


import com.knd.front.dto.VoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class VoSaveFoodType {
    //userId从token获取
    private String userId;
    @Size(max = 64)
    @ApiModelProperty("食物id")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty(value = "图标")
    private VoUrl picAttachUrl;
    @ApiModelProperty("1新增2修改")
    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;

    @ApiModelProperty(value = "子项明细")
    private List<VoSaveFoodItem> itemList;
}
