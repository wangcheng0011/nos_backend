package com.knd.manage.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class VoSaveRolePower {
    private String userId;
    @NotBlank
    @Size(max = 64)
    private String roleId;
    private List<String> powerList;
}
