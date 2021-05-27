package com.ryxt.service;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.DuplicateQuery;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;

import javax.servlet.http.HttpServletResponse;
/**
* @Description: 重复查询统计表
* @Author: uenpeng
* @Date: 2020/11/27
*/
public interface DuplicateQueryService {
    CommonListResponse<DuplicateQuery> getListPage(BaseInput record);

    DuplicateQuery saveOrUpdate(DuplicateQuery record);
    AjaxResponse<JSONObject> export(HttpServletResponse response, BaseInput user) throws Exception;
}
