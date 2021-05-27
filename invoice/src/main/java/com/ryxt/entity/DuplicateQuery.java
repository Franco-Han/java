package com.ryxt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryxt.base.annotation.RyExcel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@TableName(value = "duplicate_query_statistics")
public class DuplicateQuery {
    /**
     * ID
     */
    public String id;
    @RyExcel(ColName="发票名称")
    public String invoiceType;
    /**
     * 发票名称
     */
    @TableField(value = "invoice_name")
    public String invoiceName;
    /**
     * 发票代码
     */
    @RyExcel(ColName="发票代码")
    @TableField(value = "invoice_code")
    public String invoiceCode;
    /**
     * 发票号码
     */
    @RyExcel(ColName="发票号码")
    @TableField(value = "invoice_number")
    public String invoiceNumber;
    /**
     * 开票日期
     */
    @RyExcel(ColName="开票日期")
    @TableField(value = "invoice_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date invoiceDate;

    /**
     * 用户ID
     */
    @RyExcel(ColName="查验用户")
    @TableField(value = "user_id")
    public String userId;

    /**
     * 查询时间
     */
    @RyExcel(ColName="查验日期")
    @TableField(value = "create_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createDate;
    /**
     * 查验结果
     */
    @RyExcel(ColName="查验结果")
    @TableField(value = "result")
    private  String result;



}
