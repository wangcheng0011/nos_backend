package com.knd.manage.basedata.vo;

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
public class VoSavePowerTest {
    /**
     * userId从token获取
     */
    private String userId;

    @NotBlank(message = "操作类型不可空")
    @Pattern(regexp = "^(1|2)$", message = "操作类型错误")
    @ApiModelProperty(value = "操作类型1新增 2更新")
    private String postType;

    @Size(max = 64)
    @ApiModelProperty(value = "力量测试id,更新得时候必输")
    private String powerTestId;

    @ApiModelProperty(value = "性别 0男1女")
    @Size(max = 1, message = "性别最大长度为1")
    @NotBlank(message = "性别不可空")
    private String gender;

    @ApiModelProperty(value = "难度ID")
    @Size(max = 64, message = "难度ID最大长度为64")
    @NotBlank(message = "难度ID不可空")
    private String difficultyId;

    @ApiModelProperty(value = "图标")
    private VoUrl picAttachUrl;

    @ApiModelProperty(value = "总测试时长(秒)")
    @Size(max = 11, message = "测试时长最大长度为11")
    private String totalDuration;

    @ApiModelProperty(value = "排序")
    @Size(max = 11, message = "排序最大长度为11")
    private String sort;

    @ApiModelProperty(value = "子项明细")
    @Valid
    private List<VoSavePowerTestItem> itemList;

}
