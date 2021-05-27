package com.ryxt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryxt.base.annotation.RyExcel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName(value = "common_seller_statistics")
public class CommonSeller {
    /**
     * ID
     */
    public String id;


    /**
     * 购买方名称
     */
    @RyExcel(ColName="购买方名称")
    @TableField(value = "buyer_name")
    public String buyerName;

    /**
     * 销售方名称
     */
    @RyExcel(ColName="销售方名称")
    @TableField(value = "seller_name")
    public String sellerName;

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
     *开票日期
     */
    @RyExcel(ColName="开票日期")
    @TableField(value = "invoice_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date invoiceDate;

    /**
     * 查验日期
     */
    @RyExcel(ColName="查验日期")
    @TableField(value = "invoice_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public String createDate;
    /**
     * 价款合计金额
     */
    @RyExcel(ColName="价款合计金额")
    @TableField(value = "price")
    public String price;

    /**
     * 税额合计金额
     */
    @RyExcel(ColName="税额合计金额")
    @TableField(value = "price_tax")
    public String priceTax;

    /**
     * 价税合计金额
     */
    @RyExcel(ColName="价税合计金额")
    @TableField(value = "tax_amount")
    public String taxAmount;
}
