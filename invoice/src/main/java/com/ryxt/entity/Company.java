package com.ryxt.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@Data
@TableName(value = "company")
public class Company  {
    @TableId
    private String id          ;
    @TableField(value = "user_id")
    private String userId    ;


    @TableField(value = "create_date")
    @JsonFormat(shape= JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate    ;
    @TableField(value = "company_name")
    private String companyName    ;
    private String tin        ;
    @TableField(value = "address_and_telephone")
    private String addressAndTelephone      ;
    @TableField(value = "account_bank_and_number")
    private String accountBankAndNumber    ;
    private String remarks         ;
    @TableField(value = "delete_flag")
    private String deleteFlag       ;
    @TableField(value = "status")
    private String  status   ;
    @TableField(value = "buyerSellerName")
    private String buyerSellerName;


}
