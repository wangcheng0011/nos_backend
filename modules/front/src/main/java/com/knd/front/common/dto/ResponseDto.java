package com.knd.front.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
@Builder
public class ResponseDto<T> {
    //总数
    private int total;
    //列表
    private List<T> resList;


}
