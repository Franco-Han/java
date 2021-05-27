package com.ryxt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* @Description: 开票区间
* @Author: uenpeng
* @Date: 2020/10/15
*/
@Data

@TableName("invoice_date")
public class InvoiceDate {
    /**
     ID
     */
    private String id;	
    /**
     开始日期
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "start_date")
    private Date startDate;
    /**
     结束日期
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "end_date")
    private Date endDate;
    /**
     用户id
     */
    @TableField(value = "user_id")
    private String userId;
    /**
     创建日期
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_date")
    private Date createDate;

    /**
     是否删除
     */
    private String deleteFlag;
}
