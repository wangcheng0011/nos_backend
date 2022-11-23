package com.knd.manage.equip.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class VoSaveEquipment {
    //userId从token获取
    private String userId ;
    @NotBlank
    @Size(max = 64)
    private String equipmentNo;
    @ApiModelProperty(value = "序列号")
    private String serialNo;
    @NotBlank
    @Size(max = 256)
    private String remark;
    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;
    @Size(max = 64)
    private String id;

}
