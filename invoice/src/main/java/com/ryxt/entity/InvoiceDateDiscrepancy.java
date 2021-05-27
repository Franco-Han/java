package com.ryxt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryxt.base.annotation.RyExcel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName(value = "invoice_date_discrepancy_statistics")
public class InvoiceDateDiscrepancy {
    /**
     * ID
     */
    public String id;
    /**
     * 发票名称
     */
    @RyExcel(ColName="发票名称")
    @TableField(value="invoice_Type")
    public String invoiceType;
    /**
     * 发票代码
     */
    @RyExcel(ColName="发票代码")
    @TableField(value="invoice_code")
    public String invoiceCode;
    /**
     *发票号码
     */
    @RyExcel(ColName="发票号码")
    @TableField(value="invoice_number")
    public String invoiceNumber;
    /**
     * 开票日期
     */
    @RyExcel(ColName="开票日期")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value="invoice_date")
    public Date invoiceDate;
    /**
     * 查询日期
     */
    @RyExcel(ColName="查验日期")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value="create_date")
    public Date createDate;
}
