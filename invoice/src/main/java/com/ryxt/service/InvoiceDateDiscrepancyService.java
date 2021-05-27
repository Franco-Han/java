package com.ryxt.service;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.CheckResult;
import com.ryxt.entity.InvoiceDateDiscrepancy;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;

import javax.servlet.http.HttpServletResponse;
/**
* @Description: 开票日期不符
* @Author: uenpeng
* @Date: 2020/11/27
*/
public interface InvoiceDateDiscrepancyService {
    CommonListResponse<CheckList> getListPage(BaseInput record);

    InvoiceDateDiscrepancy saveOrUpdate(InvoiceDateDiscrepancy record);
    AjaxResponse<JSONObject> export(HttpServletResponse response, BaseInput user) throws Exception;
}
