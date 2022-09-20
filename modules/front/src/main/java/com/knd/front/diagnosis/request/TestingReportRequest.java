package com.knd.front.diagnosis.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * <p>
 * 
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */

@Data
public class TestingReportRequest {

    @NotBlank(message = "设备号不能为空")
    private String equipmentNo;
    @NotBlank(message = "检修人员id不能为空")
    private String repairmanId;

    @NotBlank(message = "用户签名文件路径名称不能为空")
    private String autographFilePathName;

    @NotBlank(message = "用户签名文件名称不能为空")
    private String autographFileName;

    @Size(max = 32, message = "签名图片大小最大长度为32")
    //签名图片大小
    private String autographFileSize;

    @Valid
    private LinkedList<TestingReportDetailRequest> testingReportDetailRequestList;




}
