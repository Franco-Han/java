package com.ryxt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryxt.base.annotation.RyExcel;
import com.ryxt.base.annotation.RyParams;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class CheckListOcrVo {
    //网站数据
    /**
     ID
     */
    private String id;
    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String userId;
    /**
     * 图片地址
     */
    private String url;
    /**
     * 查验状态
     */
    private String status;
    /**
     * ocrID
     */
    private String ocrId;
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
     开票日期
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @RyExcel(ColName="开票日期")
    private Date invoiceDate;
    /**
     查询日期
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @RyExcel(ColName="查验日期")
    private Date createDate;
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
     发票种类（增值税电子普通发票；增值税普通发票；增值税专用发票）
     */
    @TableField(value = "invoice_type")
    @RyExcel(ColName="发票类型")
    private String invoiceType;
    /**
     录入方式
     */
    @RyExcel(ColName="录入方式")
    private String checkType;
    /**
     校验码（增值税专用发票无校验码）
     */
    @RyExcel(ColName="校验码")
    private String checkCode;
    /**
     * 是否复查
     */
    @TableField(value = "re_check")
    @RyExcel(ColName="是否复查")
    private Boolean reCheck;

    //OCR数据
    /**
     * 机器编号
     */
    /**
     * OCR机器编号
     */
    /**
     购买方名称
     */
    @RyExcel(ColName="购买方名称")
    private String purchaser;
    /**
     *OCR购买方名称
     */
    @RyExcel(ColName="OCR购买方名称")
    private String ocrPurchaser;
    /**
     纳税人识别号
     */
    @RyExcel(ColName="纳税人识别号(购买方)")
    @RyParams(key = "tin",value = "纳税人识别号")
    private String tin;
    /**
     * ocr纳税人识别号
     */
    @RyExcel(ColName="OCR纳税人识别号(购买方)")
    @RyParams(key = "ocrTin",value = "ocr纳税人识别号")
    private String ocrTin;

    /**
     地址电话(购买方)
     */
    @RyExcel(ColName="地址电话(购买方)")
    @RyParams(key = "addressAndTelephone",value = "地址电话")
    private String addressAndTelephone;
    /**
     OCR地址电话(购买方)
     */
    @RyExcel(ColName="OCR地址电话(购买方)")
    @RyParams(key = "orcAddressAndTelephone",value = "地址电话")
    private String orcAddressAndTelephone;
    /**
     开户行及账号
     */
    @RyExcel(ColName="开户行及账号(购买方)")
    @RyParams(key = "accountBankAndNumber",value = "开户行及账号")
    private String accountBankAndNumber;
    /**
     OCR开户行及账号
     */
    @RyExcel(ColName="OCR开户行及账号(购买方)")
    @RyParams(key = "ocrAccountBankAndNumber",value = "开户行及账号")
    private String ocrAccountBankAndNumber;
    /**
     货物或应税劳务服务名称
     */
    @RyExcel(ColName="货物或应税劳务服务名称等")
    @RyParams(key = "nameOfGoodsOrTaxableServices",value = "货物或应税劳务服务名称")
    private String nameOfGoodsOrTaxableServices;
    /**
     OCR货物或应税劳务服务名称
     */
    @RyExcel(ColName="OCR货物或应税劳务服务名称等")
    @RyParams(key = "ocrNameOfGoodsOrTaxableServices",value = "货物或应税劳务服务名称")
    private String ocrNameOfGoodsOrTaxableServices;
    /**
     规格型号
     */
    @RyExcel(ColName="规格型号")
    @RyParams(key = "specificationType",value = "规格型号")
    private String specificationType;
    /**
     OCR规格型号
     */
    @RyExcel(ColName="OCR规格型号")
    @RyParams(key = "ocrSpecificationType",value = "OCR规格型号")
    private String ocrSpecificationType;
    /**
     单位
     */
    @RyExcel(ColName="单位")
    @RyParams(key = "unit",value = "单位")
    private String unit;
    /**
     OCR单位
     */
    @RyExcel(ColName="OCR单位")
    @RyParams(key = "ocrUnit",value = "OCR单位")
    private String ocrUnit;
    /**
     数量
     */
    @RyExcel(ColName="数量")
    @RyParams(key = "quantity",value = "数量")
    private String quantity	;
    /**
     OCR数量
     */
    @RyExcel(ColName="OCR数量")
    @RyParams(key = "ocrQuantity",value = "OCR数量")
    private String ocrQuantity	;
    /**
     单价
     */
    @RyExcel(ColName="单价")
    @RyParams(key = "price",value = "单价")
    private String price;
    /**
     OCR单价
     */
    @RyExcel(ColName="OCR单价")
    @RyParams(key = "ocrPrice",value = "OCR单价")
    private String ocrPrice;
    /**
     不含税价款金额
     */
    @RyExcel(ColName="不含税价款金额")
    private String excludingTaxPrice;
    /**
     OCR不含税价款金额
     */
    @RyExcel(ColName="OCR不含税价款金额")
    private String ocrExcludingTaxPrice;
    /**
     税率
     */
    @RyExcel(ColName="税率")
    @RyParams(key = "taxRate",value = "税率")
    private String taxRate	;
    /**
     OCR税率
     */
    @RyExcel(ColName="OCR税率")
    @RyParams(key = "taxRate",value = "OCR税率")
    private String ocrTaxRate	;
    /**
     税额
     */
    @RyExcel(ColName="税额")
    @RyParams(key = "taxAmount",value = "税额")
    private Double taxAmount;
    /**
     OCR税额
     */
    @RyExcel(ColName="OCR税额")
    @RyParams(key = "ocrTaxAmount",value = "OCR税额")
    private Double ocrTaxAmount;
    /**
     价款合计金额
     */
    @RyExcel(ColName="价款合计金额")
    @RyParams(key = "totalAmount",value = "价款合计金额")
    private String totalAmount;
    /**
     OCR价款合计金额
     */
    @RyExcel(ColName="OCR价款合计金额")
    @RyParams(key = "ocrTotalAmount",value = "OCR价款合计金额")
    private String ocrTotalAmount;
    /**
     税额合计金额
     */
    @RyExcel(ColName="税额合计金额")
    @RyParams(key = "totalAmountOfTax",value = "税额合计金额")
    private String totalAmountOfTax;
    /**
     OCR税额合计金额
     */
    @RyExcel(ColName="OCR税额合计金额")
    @RyParams(key = "ocrTotalAmountOfTax",value = "OCR税额合计金额")
    private String ocrTotalAmountOfTax;
    /**
     * 价税合计金额（大写）
     */

    /**
     * OCR价税合计金额（大写）
     */

    /**
     价税合计金额（小写）
     */
    @RyExcel(ColName="价税合计金额（小写）")
    @RyParams(key = "totalAmountOfPriceAndTax",value = "价税合计金额（小写）")
    private String totalAmountOfPriceAndTax;
    /**
     OCR价税合计金额（小写）
     */
    @RyExcel(ColName="OCR价税合计金额（小写）")
    @RyParams(key = "ocrTotalAmountOfPriceAndTax",value = "OCR价税合计金额（小写）")
    private String ocrTotalAmountOfPriceAndTax;
    /**
     销售方名称
     */
    @RyExcel(ColName="销售方名称")
    private String sellerName;
    /**
     OCR销售方名称
     */
    @RyExcel(ColName="OCR销售方名称")
    private String ocrSellerName;
    @RyExcel(ColName="纳税人识别号(销售方)")
    @RyParams(key = "sellerTin",value = "纳税人识别号")
    private String sellerTin;
    /**
     * ocr纳税人识别号
     */
    @RyExcel(ColName="OCR纳税人识别号(销售方)")
    @RyParams(key = "ocrSellerTin",value = "ocr纳税人识别号")
    private String ocrSellerTin;
    /**
     地址电话(销售方)
     */
    @RyExcel(ColName="地址电话(销售方)")
    @RyParams(key = "sellerAddressAndTelephone",value = "地址电话")
    private String sellerAddressAndTelephone	;
    /**
     OCR地址电话(销售方)
     */
    @RyExcel(ColName=" OCR地址电话(销售方)")
    @RyParams(key = "ocrSellerAddressAndTelephone",value = " OCR地址电话(销售方)")
    private String ocrSellerAddressAndTelephone	;
    /**
     开户行及账号(销售方)
     */
    @RyExcel(ColName="开户行及账号(销售方)")
    @RyParams(key = "sellerAccountBankAndNumber",value = "开户行及账号")
    private String sellerAccountBankAndNumber;
    /**
     开户行及账号(销售方)
     */
    @RyExcel(ColName="OCR开户行及账号(销售方)")
    @RyParams(key = "ocrSellerAccountBankAndNumber",value = "开户行及账号(销售方)")
    private String ocrSellerAccountBankAndNumber;
    /**
     备注
     */
    @RyExcel(ColName="备注")
    @RyParams(key = "remarks",value = "备注")
    private String remarks;
    /**
     OCR备注
     */
    @RyExcel(ColName="OCR备注")
    @RyParams(key = "ocrRemarks",value = "OCR备注")
    private String ocrRemarks;
    /**
     收款人
     */
    @RyExcel(ColName="收款人")
    @RyParams(key = "payee",value = "收款人")
    private String payee;
    /**
     OCR收款人
     */
    @RyExcel(ColName="OCR收款人")
    @RyParams(key = "ocrPayee",value = "OCR收款人")
    private String ocrPayee;
    /**
     复核
     */
    @RyExcel(ColName="复核")
    @RyParams(key = "review",value = "复核")
    private String review;
    /**
     OCR复核
     */
    @RyExcel(ColName="OCR复核")
    @RyParams(key = "ocrReview",value = "OCR复核")
    private String ocrReview;
    /**
     开票人
     */
    @RyExcel(ColName="开票人")
    @RyParams(key = "drawer",value = "开票人")
    private String drawer;
    /**
     OCR开票人
     */
    @RyExcel(ColName="OCR开票人")
    @RyParams(key = "ocrDrawer",value = "OCR开票人")
    private String ocrDrawer;

    /**
     * 删除状态
     */
    private String deleteFlag;
}
