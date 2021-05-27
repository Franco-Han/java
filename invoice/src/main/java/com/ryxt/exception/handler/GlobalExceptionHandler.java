package com.ryxt.exception.handler;

import javax.servlet.http.HttpServletRequest;

import com.ryxt.util.AjaxResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK) 
    @ResponseBody
    public AjaxResponse<Exception> controllerExceptionHandler(HttpServletRequest req, Exception e) {
//        return AjaxResponse.fail(HttpStatus.SEE_OTHER.value(), "未知错误", e);
        return AjaxResponse.fail(HttpStatus.SEE_OTHER.value(), e.getMessage(), e);
    }
}
