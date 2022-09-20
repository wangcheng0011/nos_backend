package com.knd.common.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * order状态
 * @author zm
 */
@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
public enum InstallOperationEnum {

    /**
     * 1->接受任务;2->开始安装；3->已确认安装
     */
    ACCEPT_TASK("1","接受任务"),
    START_INSTALL("2","开始安装"),
    CONFIRM_INSTALL("3","已确认安装");

    private final String code;
    private final String message;
}
