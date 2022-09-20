package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class VoSaveDifficulty {
    @NotBlank
    @Size(max = 32)
    private String type;
    @NotBlank
    @Size(max = 32)
    private String difficulty;
    @NotBlank
    @Size(max = 256)
    private String remark;

    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;

    @Size(max = 64)
    private String difficultyId;

    private VoUrl imageUrl;

    private String userId;

}
