package com.knd.batch.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TrainWeek {
    //周开始日期
    private LocalDate beginDate;
    //周结束日期
    private LocalDate endDate;

}
