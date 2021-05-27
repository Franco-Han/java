package com.ryxt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* @Description: 查验结果实体
* @Author: uenpeng
* @Date: 2020/10/12
*/
@Data

@TableName("check_result")
public class CheckResult {
    /**
     ID
     */
    private String id;	
    /**
     发票种类（增值税电子普通发票；增值税普通发票；增值税专用发票）
     */
    @TableField(value = "invoice_type")
    private String invoiceType;	
    /**
     发票代码
     */
    @TableField(value = "invoice_code")
    private String invoiceCode;	
    /**
     发票号码
     */
    @TableField(value = "invoice_number")
    private String invoiceNumber;
    /**
     校验码（增值税专用发票无校验码）
     */
    @TableField(value = "check_code")
    private String checkCode;
    /**
     不含税价款金额
     */
    @TableField(value = "excluding_tax_price")
    private String excludingTaxPrice;
    /**
     开票日期
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "invoice_date")
    private Date invoiceDate;
    /**
     创建日期
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_date")
    private Date createDate;
    /**
     处理状态
     */
    private String status;
    /**
     查验结果
     */
    private String result;
    /**
     失败原因
     */
    private String reason;
    /**
     图片地址
     */
    private String url	;
    /**
     用户id
     */
    @TableField(value = "user_id")
    private String userId;
    /**
     查验次数
     */
    private int count;	
    /**
     购买方名称
     */
    private String purchaser;	
    /**
     纳税人识别号
     */
    private String tin;
    /**
     地址电话
     */

    @TableField(value = "address_and_telephone")
    private String addressAndTelephone;	

    /**
     开户行及账号
     */
    @TableField(value = "account_bank_and_number")
    private String accountBankAndNumber;

    /**
     货物或应税劳务服务名称
     */
    @TableField(value = "name_of_goods_or_taxable_services")
    private String nameOfGoodsOrTaxableServices;	
    /**
     规格型号
     */
    @TableField(value = "specification_type")
    private String specificationType;	
    /**
     单位
     */
    
    private String unit;	
    /**
     数量
     */
    
    private String quantity	;
    /**
     单价
     */
    
    private String price;	
    /**
     税率
     */

    @TableField(value = "tax_rate")
    private String taxRate	;
    /**
     税额
     */

    @TableField(value = "tax_amount")
    private String taxAmount;	
    /**
     价款合计金额
     */

    @TableField(value = "total_amount")
    private String totalAmount;	
    /**
     税额合计金额
     */

    @TableField(value = "total_amount_of_tax")
    private String totalAmountOfTax;	
    /**
     价税合计金额（小写即可）
     */

    @TableField(value = "total_amount_of_price_and_tax")
    private String totalAmountOfPriceAndTax;	
    /**
     销售方名称
     */
    @TableField(value = "seller_name")
    private String sellerName;	
    /**
     纳税人识别号
     */
    @TableField(value = "seller_tin")
    private String sellerTin;	
    /**
     地址电话
     */
    @TableField(value = "seller_address_and_telephone")
    private String sellerAddressAndTelephone	;
    /**
     开户行及账号
     */
    @TableField(value = "seller_account_bank_and_number")
    private String sellerAccountBankAndNumber;	
    /**
     备注
     */
    private String remarks;	
    /**
     收款人
     */
    private String payee;	
    /**
     复核
     */
    private String review;	
    /**
     开票人
     */
    private String drawer;	
    /**
     父ID
     */
    @TableField(value = "parent_id")
    private  String parentId;

    /**
     金额
     */
    private double amount;
}
