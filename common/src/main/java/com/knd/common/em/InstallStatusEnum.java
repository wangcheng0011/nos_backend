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
public enum InstallStatusEnum {
    /**
     * 0->待分配；1->待接受;2->待安装；3->安装中；4->已确认安装
     */
    WAIT_DISTRIBUTED("0","待分配"),
    WAIT_ACCEPTED("1","待接受"),
    WAIT_INSTALLED("2","待安装"),
    INSTALLATION("3","安装中"),
    CONFIRM_INSTALL("4","已确认安装");

    private final String code;
    private final String message;

}
