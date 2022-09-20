package com.knd.front.train.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/7
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainCourseBlockInfoRequest {
    @NotBlank(message = "训练blockid不能为空")
    private String blockId;
    @NotBlank(message = "训练名称不能为空")
    private String block;
    @NotBlank(message = "训练组序号不能为空")
    private String blockSetNum;
    @NotBlank(message = "训练完成组数不能为空")
    private String sets;
    @Valid
    private LinkedList<TrainCourseActInfoRequest> actionList;
}