package com.ryxt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.InformationComparison;
import com.ryxt.entity.InformationDiscrepancy;
import com.ryxt.service.InformationDiscrepancyService;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 购买方/销售方不符统计表
 */
@RestController
@RequestMapping("idc")
public class InformationDiscrepancyController {
    @Autowired
    private InformationDiscrepancyService informationDiscrepancyService;
    /**
     * 获取列表 分页
     * @param record
     * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */
    @RequestMapping("/getListPage")
    public CommonListResponse<InformationDiscrepancy> getListPage(@RequestBody BaseInput record) {
        return informationDiscrepancyService.getListPage(record);
    }
    /**
     * 导出excel
     * @param record
     * @throws Exception
     */
    @RequestMapping("/export")
    public AjaxResponse<JSONObject> export(@RequestBody BaseInput record, HttpServletResponse response) throws Exception {
        return  informationDiscrepancyService.export(response,record);
    }
}
