package com.knd.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 在自定义异常的错误码和信息时，如果过多，没有统一管理，则会出现重复。
 * 使用枚举统一管理code和message：
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
    SUCCESS("U0001", "成功"),

    /***
     * 所有接口成功都返回，如非成功的请在下面自定义
     */
    VALID_ERROR("0001", "验证错误"),

    UNKNOWN_ERROR("9999", "系统异常，请稍后再试"),
    /**
     * 文件为空
     */
    FILE_NULL_ERROR("U0996", "文件不能为空"),
    PWD_REPEAT_ERROR("U0842","新密码与原密码不能相同"),
    FILE_NONE_ERROR("U0997", "不支持此文件"),
    FILE_SIZE_ERROR("U9991", "文件不能超过500M"),
    PWD_MOBILE_ERROR("U1901", "手机号或密码不正确"),
    DELETE_PHONE_ERROR("U1903", "该手机号还未注册，请用验证码注册并登录"),
    NOT_PHONE_ERROR("U1902", "手机号不存在"),
    VERIFY_CODE_ERROR("U1001", "无效验证码"),
    EXIST_PHONE_ERROR("A0003", "该手机号已存在"),
    ACCOUNT_FREEZE_ERROR("U1902", "该账号已被冻结"),
    SERVICE_KEY_ERROR("U0900", "业务主键重复"),
    BLOCK_NODE_ERROR("U0993", "有关联的视频小节，不可删除"),
    CODE_TIME_OUT("U5777","验证码已过期,请重新获取"),
    FILE_OBS_URL_ERROR("U0986", "obs找不到对象"),
    Type_ERROR("U0987", "有课程绑定该类型，不可删除"),
    Action_ERROR("U0988", "有课程绑定该动作，不可删除"),
    Equip_ERROR("U0990", "有动作绑定该配件，不可删除"),
    Part_ERROR("U0991", "有课程绑定该部位，不可删除"),
    Part_ERROR_2("U0992", "有动作绑定该部位，不可删除"),
    Target_ERROR("U0993", "有课程绑定该目标，不可删除"),
    Target_ERROR_2("U0994", "有动作绑定该目标，不可删除"),
    PARAM_ERROR("U0995", "参数校验失败"),
    FAIL("U0999", "失败"),
    SQL_ERROR("U0500", "数据库异常，事务回滚成功"),
    BLOCK_ACTION_TOOMORE("U0910", "分组下维护动作数量太多"),
    SERVICE_FUSE("U0998", "服务熔断"),
    UNAUTHORIZED("401", "身份验证未通过，或者验证信息失效"),
    ROLE_DELETE_ERROR("U970","有用户绑定该角色，不可删除"),
    USER_FROZEN_ERROR("U8001","该用户已被冻结"),
    USER_DELETE_ERROR("U8002","该用户已被删除"),
    USER_NOT_FOUND_ERROR("U8003","该用户不存在"),
    COACH_KEY_ERROR("U8004", "教练标签重复");

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}