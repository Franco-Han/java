package com.ryxt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.ReviewResultChange;
import com.ryxt.service.ReviewResultChangeService;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 复查结果变更统计表
 */
@RestController
@RequestMapping(value = "rrcs")
public class ReviewResultChangeController {

    @Autowired
    private ReviewResultChangeService reviewResultChangeService;

    @RequestMapping("/getListPage")
    public CommonListResponse<ReviewResultChange> getListPage(@RequestBody BaseInput record) {
        return reviewResultChangeService.getListPage(record);
    }
    /**
     * 导出excel
     * @param record
     * @throws Exception
     */
    @RequestMapping("/export")
    public AjaxResponse<JSONObject> export(@RequestBody BaseInput record, HttpServletResponse response) throws Exception {
        return  reviewResultChangeService.export(response,record);
    }
}
