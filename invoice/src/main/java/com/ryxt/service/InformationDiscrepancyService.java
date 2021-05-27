package com.ryxt.service;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.InformationComparison;
import com.ryxt.entity.InformationDiscrepancy;
import com.ryxt.entity.InvoiceDateDiscrepancy;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;

import javax.servlet.http.HttpServletResponse;
/**
 * 购买方/销售方不符统计表
 */
public interface InformationDiscrepancyService {
    InformationDiscrepancy saveOrUpdate(InformationDiscrepancy record);
    CommonListResponse<InformationDiscrepancy> getListPage(BaseInput record);
    AjaxResponse<JSONObject> export(HttpServletResponse response, BaseInput user) throws Exception;
}
