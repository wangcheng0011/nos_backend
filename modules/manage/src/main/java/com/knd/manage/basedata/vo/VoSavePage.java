package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class VoSavePage {
    private String userId;

    @NotBlank
    @Size(max = 64)
    private String keyValue;

    @NotBlank
    @Size(max = 64)
    private String version;

    @NotBlank
    private String pageType;

    @Size(max = 256)
    private String pageName;

    private String pageDetail;

    private VoUrl imageUrl;

    private VoUrl backgroundUrl;

    private String platform;
    private String skipUrl;
    private String searchUrl;

    private VoUrl showUrl;
    private String description;

    @Size(max = 64)
    private String pageId;

    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;

    private List<VoSort> floorList;
}
