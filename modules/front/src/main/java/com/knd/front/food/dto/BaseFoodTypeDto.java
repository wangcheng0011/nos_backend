package com.knd.front.food.dto;


import com.knd.front.pay.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author zm
 */
@Data
public class BaseFoodTypeDto {


    @Size(max = 64)
    @ApiModelProperty("食物id")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty(value = "图标")
    private ImgDto picAttachUrl;
    @ApiModelProperty("1新增2修改")
    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;
    @ApiModelProperty(value = "子项明细")
    @Valid
    private List<FoodDto> itemList;



}