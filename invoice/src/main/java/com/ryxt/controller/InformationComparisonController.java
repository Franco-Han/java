package com.ryxt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.InformationComparison;
import com.ryxt.entity.InvoiceDateDiscrepancy;
import com.ryxt.service.InformationComparisonService;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 票面信息比对统计表控制器
 */
@RestController
@RequestMapping("icc")
public class InformationComparisonController {
    @Autowired
    private InformationComparisonService informationComparisonService;

    /**
     * 获取列表 分页
     * @param record
     * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */
    @RequestMapping("/getListPage")
    public CommonListResponse<InformationComparison> getListPage(@RequestBody BaseInput record) {
        return informationComparisonService.getListPage(record);
    }
    /**
     * 导出excel
     * @param record
     * @throws Exception
     */
    @RequestMapping("/export")
    public AjaxResponse<JSONObject> export(@RequestBody BaseInput record, HttpServletResponse response) throws Exception {
        return  informationComparisonService.export(response,record);
    }
}
