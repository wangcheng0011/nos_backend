package com.knd.manage.basedata.vo;

import com.knd.manage.basedata.dto.UserModelDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class   VoSaveLabel {
    private String userId;
    @NotBlank
    @Size(max = 32)
    private String type;
    @NotBlank
    @Size(max = 32)
    private String label;
    @NotBlank
    @Size(max = 256)
    private String remark;

    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;

    @Size(max = 64)
    private String labelId;

    private VoUrl imageUrl;

    private List<UserModelDto> userModelList;

}
