package com.ryxt.entity;


import lombok.Data;
/**
* @Description: 修改密码
* @Author: uenpeng
* @Date: 2020/11/4
*/
@Data
public class Password {

    private String oldPassword    ;
    private String newPassword    ;
    private String userId    ;
}
