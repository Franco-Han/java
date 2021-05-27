package com.ryxt.service;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.*;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;

import javax.servlet.http.HttpServletResponse;
/**
* @Description: 同一销售方
* @Author: uenpeng
* @Date: 2020/11/27
*/
public interface CommonSellerService {
    CommonListResponse<CommonSeller> getListPage(BaseInput record);
    CommonSeller saveOrUpdate(CommonSeller record);
    AjaxResponse<JSONObject> export(HttpServletResponse response, BaseInput record) throws Exception;
}
