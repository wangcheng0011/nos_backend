package com.knd.manage.course.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class VoUpdateCourseBlocksSort {
    private String userId;
    @NotEmpty
    List<BlockSort> blockSortList;
}
