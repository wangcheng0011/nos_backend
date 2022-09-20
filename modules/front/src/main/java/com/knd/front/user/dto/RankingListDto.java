package com.knd.front.user.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
@Builder
public class RankingListDto {

    @ApiModelProperty(value = "用户信息")
    private UserTrainDto userTrainDto;

    @ApiModelProperty(value = "排行榜信息")
    private List<UserTrainDto> userTrainList;
}
