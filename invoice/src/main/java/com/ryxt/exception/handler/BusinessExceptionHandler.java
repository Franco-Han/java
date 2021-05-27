package com.ryxt.exception.handler;

import javax.servlet.http.HttpServletRequest;

import com.ryxt.exception.BusinessException;
import com.ryxt.util.AjaxResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK) 
    @ResponseBody
    public AjaxResponse<BusinessException> controllerExceptionHandler(HttpServletRequest req, BusinessException e) {
        return AjaxResponse.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e);
    }

    @ResponseBody
    @ExceptionHandler(value = OAuth2Exception.class)
    public AjaxResponse<OAuth2Exception> handleOauth2(OAuth2Exception e) {
        return AjaxResponse.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage(), e);
    }
}
