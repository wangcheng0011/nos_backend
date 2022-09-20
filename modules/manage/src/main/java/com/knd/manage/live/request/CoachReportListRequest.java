package com.knd.manage.live.request;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class CoachReportListRequest {
    @Size(max = 64)
    private String timeId;
    @Size(max = 64)
    private String reportUserId;
    private String current;
}
