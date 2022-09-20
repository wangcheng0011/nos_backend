package com.knd.manage.course.dto;

import lombok.Data;

@Data
public class BlockDataDto {
    //上一个block序号
    private String lastBlockSort;
    //下一个block序号
    private String nextBlockSort;
    //当前block序号
    private String nowBlockSort;
}
