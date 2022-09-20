package com.knd.front.train.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zm
 */
@Data
@ApiModel(value="Attach", description="")
@Builder
public class VideoUrlDto {

    @ApiModelProperty(value = "filePath")
    private String filePath;

    @ApiModelProperty(value = "fileSize")
    private String fileSize;
}
