package com.knd.front.train.dto;

import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Data
public class BlockDto {
    private String blockId;
    private String block;
    private String aimSetNum;
    private List<ActionDto> actionList;

}