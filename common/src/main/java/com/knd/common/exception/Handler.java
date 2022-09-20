package com.knd.common.exception;

import com.knd.common.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * on 2018/3/13.
 */
@Slf4j
@RestControllerAdvice
@CrossOrigin
public class Handler {

    /**
     * 运行时异常
     *
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public Result handlweOtherExceptions(Exception e, WebRequest request, HttpServletResponse response) {

        StringBuffer message = new StringBuffer();
        if (request instanceof ServletWebRequest) {
            HttpServletRequest hsr = ((ServletWebRequest) request).getRequest();
            message.append("【运行时异常】" + ResultEnum.UNKNOWN_ERROR.getCode() + ", ");
            message.append("URL: " + hsr.getRequestURL());
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            message.append(sw.toString());
            log.error(message.toString());
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
    }

    /**
     * 自定义异常
     *
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler(value = {CustomResultException.class})
    @ResponseBody
    public Result CustomException(CustomResultException e, WebRequest request, HttpServletResponse response) {

        StringBuffer message = new StringBuffer();
        if (request instanceof ServletWebRequest) {
            HttpServletRequest hsr = ((ServletWebRequest) request).getRequest();
            message.append("【自定义异常】" + e.getCode() + ", ");
            message.append("URL: " + hsr.getRequestURL());
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            message.append(sw.toString());
            log.error(message.toString());
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResultUtil.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = {JobException.class})
    public void JobException() {
        System.out.println("---------------------看到我吗，要回滚啦啦啦啦");
    }
}
