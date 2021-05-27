package com.ryxt.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryxt.base.annotation.RyExcel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class ExcelTemplate {
    /**
     * 发票代码
     */
    @RyExcel(ColName="发票代码")
    private String invoiceCode;
    /**
     * 发票号码
     */
    @RyExcel(ColName="发票号码")
    private String invoiceNumber;
    /**
     * 发票类型
     */
    @RyExcel(ColName="发票类型")
    private String invoiceType;
    /**
     * 开票日期
     */

    @RyExcel(ColName="开票日期")
    private String invoiceDate;
    /**
     * 开具金额（不含税）
     */

    @RyExcel(ColName="开具金额（不含税）")
    private String excludingTaxPrice;
    /**
     * 校验码后六位
     */
    @RyExcel(ColName="校验码后六位")
    private String checkCode;
}