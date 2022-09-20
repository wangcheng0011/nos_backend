package com.knd.manage.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class VoSaveRole {
    private String userId;
    @NotBlank
    @Size(max = 20)
    private String name;
    @Size(max = 256)
    private String memo;
    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;
    @Size(max = 64)
    private String roleId;



}
