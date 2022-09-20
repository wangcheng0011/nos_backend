package com.knd.common.response;

import lombok.Data;

/**
 * 接口返回实体
 */
@Data
public class Result<T> {
    //返回码
    private String code;
    //提示信息
    private String message;
    //返回具体内容
    private T data;

}
