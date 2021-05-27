package com.ryxt.controller;

import com.ryxt.entity.InvoiceDate;
import com.ryxt.entity.User;
import com.ryxt.service.InvoiceDateService;
import com.ryxt.util.AjaxListResponse;
import com.ryxt.util.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
* @Description: 开票区间维护
* @Author: uenpeng
* @Date: 2020/10/16
*/
@RestController
@RequestMapping(value = "/invoiceDate")
public class InvoiceDateController {

    @Autowired
    private InvoiceDateService invoiceDateService;
    @RequestMapping("/getInvoiceDate")
    public AjaxListResponse<InvoiceDate> getInvoiceDate() {
        return AjaxListResponse.success(invoiceDateService.getInvoiceDate());
    }


    /**
     * 保存或更新
     * @param record
     * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.InvoiceDate>
     * @throws Exception
     */

    @RequestMapping("/saveOrUpdate")
    public AjaxResponse<InvoiceDate> saveOrUpdate(@RequestBody InvoiceDate record) {
        return AjaxResponse.success(invoiceDateService.saveOrUpdate(record));
    }
}
