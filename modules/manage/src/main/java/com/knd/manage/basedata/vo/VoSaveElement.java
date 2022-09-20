package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class VoSaveElement {
    private String userId;

    @NotBlank
    @Size(max = 64)
    private String floorId;

    @Size(max = 64)
    private String keyValue;

    @Size(max = 2)
    private String sort;

    private String elementName;
    private String elementDetail;
    private String elementNote;

    private VoUrl imageUrl;
    private VoUrl backgroundUrl;

    private String skipUrl;
    private String searchUrl;

    private VoUrl showUrl;
    private String description;

    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;

    private String elementId;
}
