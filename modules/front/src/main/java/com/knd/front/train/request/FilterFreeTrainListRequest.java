package com.knd.front.train.request;

import com.knd.front.home.dto.ListDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/2
 * @Version 1.0
 */
@Data
public class FilterFreeTrainListRequest {
    private String currentPage;
    @NotBlank(message = "每页条数不能为空")
    private String pageSize;
    private String userId;
    private List<ListDto> targetList;
    private List<ListDto> partList;
    private List<ListDto> equipmentList;
}