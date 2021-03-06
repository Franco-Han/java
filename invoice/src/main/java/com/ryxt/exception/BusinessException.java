package com.ryxt.exception;

/**
* @Description: 业务异常类
* @Author: uenpeng
* @Date: 2020/10/29
*/
public class BusinessException extends RuntimeException {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8164041950695047364L;
    

    public BusinessException(Throwable cause) {
    	super(cause);
    }
    
    public BusinessException(String message) {
    	super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    
}