package com.ryxt.util;


import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 通用Response类
 * @author uenpeng
 */
public class AjaxResponse<T> implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -966198037820471167L;

    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 状态代码
     */
    private int statusCode;
    
    /**
     * 返回消息
     */
    private String message;
    
    /**
     * 数据
     */
    private T data;
    

    public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public AjaxResponse() {
        this.success = true;
        this.statusCode = HttpStatus.OK.value();
    }
    
    public AjaxResponse(boolean success, int statusCode) {
        this.success = success;
        this.statusCode = statusCode;
    }
    
    public AjaxResponse(boolean success, int statusCode, T data) {
    	this(success, statusCode);
        this.data = data;
    }
    
    public AjaxResponse(boolean success, int statusCode, String message) {
    	this(success, statusCode);
        this.message = message;
    }
    
    public AjaxResponse(boolean success, int statusCode, String message, T data) {
        this(success, statusCode, message);
        this.data = data;
    }
    
    public static AjaxResponse<Object> success() {
        return new AjaxResponse<Object>();
    }
    
    public static <T> AjaxResponse<T> success(T data) {
        return new AjaxResponse<T>(true, HttpStatus.OK.value(), data);
    }
    
    public static AjaxResponse<Object> fail(int statusCode, String message) {
    	return new AjaxResponse<Object>(false, statusCode, message);
    }
    
    
    public static <T> AjaxResponse<T> fail(int statusCode, String message, T data) {
    	return new AjaxResponse<T>(false, statusCode, message, data);
    }
    
    public static <T> AjaxResponse<T> fail(String message, T data) {
    	return new AjaxResponse<T>(false, HttpStatus.BAD_REQUEST.value(), message, data);
    }
    
}