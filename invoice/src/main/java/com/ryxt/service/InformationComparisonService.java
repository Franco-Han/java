package com.ryxt.service;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.InformationComparison;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;

import javax.servlet.http.HttpServletResponse;
/**
 * 票面信息比对统计
 */
public interface InformationComparisonService {
    CommonListResponse<InformationComparison> getListPage(BaseInput record);
    InformationComparison saveOrUpdate(InformationComparison record);
    AjaxResponse<JSONObject> export(HttpServletResponse response, BaseInput user) throws Exception;
}
