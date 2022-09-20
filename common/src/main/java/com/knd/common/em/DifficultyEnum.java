package com.knd.common.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 课程难度id
 * @author zm
 */
@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
public enum DifficultyEnum {

    BASIS("1","基础"),
    PRIMARY("2","初级"),
    INTERMEDIATE("3","中级"),
    SENIOR("4","高级");

    private final String id;
    private final String message;


}
