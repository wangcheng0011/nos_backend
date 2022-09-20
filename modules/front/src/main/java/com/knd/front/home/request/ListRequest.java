package com.knd.front.home.request;

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
public class ListRequest {
    private List<ListDto> typeList;
}