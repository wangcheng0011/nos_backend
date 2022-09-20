package com.knd.manage.live.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class ManageRequest {

    private String userId;

    @NotBlank
    @Pattern(regexp = "^(1|2|3)$")
    private String postType;

    @Size(max = 64)
    @NotBlank
    private String id;
}
