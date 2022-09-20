package com.knd.front.train.request;

import com.knd.front.home.dto.ListDto;
import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/2
 * @Version 1.0
 */
@Data
public class FilterCourseListRequest {
    private String userId;
    private String currentPage;
    private String pageSize;
    private List<ListDto> typeList;
    private List<ListDto> targetList;
    private List<ListDto> partList;
    private String isPay;
}