package com.knd.common.response;

/**
 * 接口返回工具类
 */
public class ResultUtil {

    /**
     * 接口返回成功（无参）
     *
     * @return
     */
    public static Result success() {
        return success(null);
    }

    /**
     * 接口返回成功（有参）
     *
     * @return
     */
    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(ResultEnum.SUCCESS.getMessage());
        result.setData(object);
        return result;
    }


    /**
     * 接口异常（不建议使用）
     *
     * @param code    返回值
     * @param message 消息
     * @return
     */
    public static Result error(String code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }


    /**
     * 接口返回错误（正常返回）
     * 注； 如在枚举中未出现，请自行添加
     *
     * @param resultEnum
     * @return
     */
    public static Result error(ResultEnum resultEnum) {
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMessage(resultEnum.getMessage());
        return result;
    }

    /**
     * 结果返回错误（异常返回或需要回滚）
     *
     * @param customResultException
     * @return
     */
    public static Result error(CustomResultException customResultException) {
        Result result = new Result();
        result.setCode(customResultException.getCode());
        result.setMessage(customResultException.getMessage());
        return result;
    }

    /**
     * 是否包含错误
     *
     * @param result
     * @return
     */
    public static boolean hasError(Result result) {
        if (ResultEnum.SUCCESS.getCode().equals(result.getCode())) {
            return false;
        }
        return true;
    }
}
