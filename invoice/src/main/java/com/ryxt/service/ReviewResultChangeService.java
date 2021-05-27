package com.ryxt.service;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.ReviewResultChange;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;

import javax.servlet.http.HttpServletResponse;
/**
 * 复查结果变更统计表
 */
public interface ReviewResultChangeService {
    CommonListResponse<ReviewResultChange> getListPage(BaseInput record);
    ReviewResultChange saveOrUpdate(ReviewResultChange record);
    AjaxResponse<JSONObject> export(HttpServletResponse response, BaseInput user) throws Exception;
}
