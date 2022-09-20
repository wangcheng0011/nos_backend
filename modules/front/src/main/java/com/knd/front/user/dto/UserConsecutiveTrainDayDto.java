package com.knd.front.user.dto;

import lombok.Data;

/**
 * @author will
 */
@Data
public class UserConsecutiveTrainDayDto {
    private String beginDate;
    private String endDate;
    private String consecutiveDays;
}