package com.ryxt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryxt.base.annotation.RyExcel;
import com.ryxt.base.annotation.RyParams;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
* @Description: 查验记录实体
* @Author: uenpengcheckResults
* @Date: 2020/10/12
*/
@Data

@TableName("check_list")
public class CheckList {
    /**
     ID
     */
    private String id;
    /**
     查验方式
     */
    @TableField(value = "check_type")
    @RyExcel(ColName="查验方式")
    private String checkType;	
    /**
     发票种类（增值税电子普通发票；增值税普通发票；增值税专用发票）
     */
    @TableField(value = "invoice_type")
    @RyExcel(ColName="发票种类")
    private String invoiceType;	
    /**
     发票代码
     */
    @TableField(value = "invoice_code")
    @RyExcel(ColName="发票代码")
    private String invoiceCode;	
    /**
     发票号码
     */
    @TableField(value = "invoice_number")
    @RyExcel(ColName="发票号码")
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
    @RyExcel(ColName="不含税价款金额")
    private String excludingTaxPrice;
    /**
     开票日期
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "invoice_date")
    @RyExcel(ColName="开票日期")
    private Date invoiceDate;	
    /**
     创建日期
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_date")
    @RyExcel(ColName="查询日期")
    private Date createDate;	
    /**
     处理状态
     */
    private String status;
    /**
     查验结果
     */
    @RyExcel(ColName="查验结果")
    private String result;	
    /**
     失败原因
     */
    @RyExcel(ColName="失败原因")
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
     是否删除
     */
    @TableField(value = "delete_flag")
    private String deleteFlag;
    /**
        更新日期
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_date")
    private Date updateDate;

    /**
     ocr记录ID
     */
    @TableField(value = "ocr_id")
    private String ocrId;
    /**
     * 查验结果
     */
    @TableField(exist = false)
    private  List<CheckResult> checkResults;

    /**
     购买方名称
     */
    @RyParams(key = "purchaser",value = "购买方名称")
    @RyExcel(ColName="购买方名称")
    private String purchaser;
    /**
     纳税人识别号
     */
    @RyExcel(ColName="纳税人识别号")
    @RyParams(key = "tin",value = "购买方纳税人识别号")
    private String tin;
    /**
     地址电话
     */
    @RyExcel(ColName="地址电话(购买方)")
    @RyParams(key = "addressAndTelephone",value = "购买方地址电话")
    @TableField(value = "address_and_telephone")
    private String addressAndTelephone;

    /**
     开户行及账号
     */
    @RyExcel(ColName="开户行及账号(购买方)")
    @RyParams(key = "accountBankAndNumber",value = "购买方开户行及账号")
    @TableField(value = "account_bank_and_number")
    private String accountBankAndNumber;
    /**
     销售方名称
     */
    @RyExcel(ColName="销售方名称")
    @RyParams(key = "sellerName",value = "销售方名称")
    @TableField(value = "seller_name")
    private String sellerName;
    /**
     纳税人识别号
     */
    @RyExcel(ColName="纳税人识别号")
    @RyParams(key = "sellerTin",value = "销售方纳税人识别号")
    @TableField(value = "seller_tin")
    private String sellerTin;
    /**
     地址电话
     */
    @RyExcel(ColName="地址电话(销售方)")
    @RyParams(key = "sellerAddressAndTelephone",value = "销售方地址电话")
    @TableField(value = "seller_address_and_telephone")
    private String sellerAddressAndTelephone	;
    /**
     开户行及账号
     */
    @RyExcel(ColName="开户行及账号(销售方)")
    @RyParams(key = "sellerAccountBankAndNumber",value = "销售方开户行及账号")
    @TableField(value = "seller_account_bank_and_number")
    private String sellerAccountBankAndNumber;
    /**
     货物或应税劳务服务名称
     */
    @RyExcel(ColName="货物或应税劳务服务名称等")
    @RyParams(key = "nameOfGoodsOrTaxableServices",value = "货物或应税劳务服务名称等")
    @TableField(value = "name_of_goods_or_taxable_services")
    private String nameOfGoodsOrTaxableServices;
    /**
     规格型号
     */
    @RyExcel(ColName="规格型号")
    @TableField(value = "specification_type")
    //@RyParams(key = "specificationType",value = "规格型号")
    private String specificationType;
    /**
     单位
     */
    @RyExcel(ColName="单位")
   //@RyParams(key = "unit",value = "单位")
    private String unit;
    /**
     数量
     */
    @RyExcel(ColName="数量")
    //@RyParams(key = "addressAndTelephone",value = "地址电话")
    private String quantity	;
    /**
     单价
     */
    @RyExcel(ColName="单价")
    //@RyParams(key = "addressAndTelephone",value = "地址电话")
    private String price;
    /**
     税率
     */
    @RyExcel(ColName="税率")
    //@RyParams(key = "taxRate",value = "税率")
    @TableField(value = "tax_rate")
    private String taxRate	;
    /**
     税额
     */
    @RyExcel(ColName="税额")
    //@RyParams(key = "taxAmount",value = "税额")
    @TableField(value = "tax_amount")
    private Double taxAmount;
    /**
     价款合计金额
     */
    @RyExcel(ColName="价款合计金额")
    @RyParams(key = "totalAmount",value = "价款合计金额")
    @TableField(value = "total_amount")
    private String totalAmount;
    /**
     税额合计金额
     */
    @RyExcel(ColName="税额合计金额")
    @RyParams(key = "totalAmountOfTax",value = "税额合计金额")
    @TableField(value = "total_amount_of_tax")
    private String totalAmountOfTax;
    /**
     价税合计金额（小写即可）
     */
    @RyExcel(ColName="价税合计金额（小写）")
    @RyParams(key = "totalAmountOfPriceAndTax",value = "价税合计金额")
    @TableField(value = "total_amount_of_price_and_tax")
    private String totalAmountOfPriceAndTax;

    /**
     备注
     */
    @RyExcel(ColName="备注")
    @RyParams(key = "remarks",value = "备注")
    private String remarks;
    /**
     收款人
     */
    @RyExcel(ColName="收款人")
    //@RyParams(key = "payee",value = "收款人")
    private String payee;
    /**
     复核
     */
    @RyExcel(ColName="复核")
    //@RyParams(key = "review",value = "复核")
    private String review;
    /**
     开票人
     */
    @RyExcel(ColName="开票人")
    //@RyParams(key = "drawer",value = "开票人")
    private String drawer;

    /**
     金额
     */
    @RyExcel(ColName="金额")
    private Double amount;

    /**
     是否复查
     */
    @RyExcel(ColName="是否复查")
    @TableField(value = "re_check")
    private Boolean reCheck;

    /**
     复查id
     */
    @TableField(value = "re_check_id")
    private String reCheckId;

    /**
     原复查结果
     */
    @TableField(value = "re_check_result")
    private String reCheckResult;

    /**
     申请查验日期
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "apply_date")
    private Date applyDate;
}
