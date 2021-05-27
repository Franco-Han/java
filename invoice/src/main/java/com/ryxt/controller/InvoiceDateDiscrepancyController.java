package com.ryxt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.CheckResult;
import com.ryxt.entity.InvoiceDateDiscrepancy;
import com.ryxt.service.InvoiceDateDiscrepancyService;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 开票时间不符统计表
 */
@RestController
@RequestMapping(value = "iddc")
public class InvoiceDateDiscrepancyController {
    @Autowired
    private InvoiceDateDiscrepancyService invoiceDateDiscrepancyService;

    /**
     * 获取列表 分页
     * @param record
     * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */
    @RequestMapping("/getListPage")
    public CommonListResponse<CheckList> getListPage(@RequestBody BaseInput record) {
        return invoiceDateDiscrepancyService.getListPage(record);
    }
    /**
     * 导出excel
     * @param record
     * @throws Exception
     */
    @RequestMapping("/export")
    public AjaxResponse<JSONObject> export(@RequestBody BaseInput record, HttpServletResponse response) throws Exception {
        return  invoiceDateDiscrepancyService.export(response,record);
    }
}
