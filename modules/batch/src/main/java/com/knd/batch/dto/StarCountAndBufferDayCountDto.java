package com.knd.batch.dto;

import lombok.Data;

@Data
public class StarCountAndBufferDayCountDto {
    //星星总数
    private int starCount;
    //已消耗的缓冲天数
    private int bufferDayCount;
}
