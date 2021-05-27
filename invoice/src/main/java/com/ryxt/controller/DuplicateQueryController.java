package com.ryxt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.DuplicateQuery;
import com.ryxt.entity.InformationComparison;
import com.ryxt.service.DuplicateQueryService;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
/**
 * 重复查询统计控制器
 */
@RestController
@RequestMapping("dqc")
public class DuplicateQueryController {
    @Autowired
    private DuplicateQueryService duplicateQueryService;

    /**
     * 获取列表 分页
     * @param record
     * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */
    @RequestMapping("/getListPage")
    public CommonListResponse<DuplicateQuery> getListPage(@RequestBody BaseInput record) {
        return duplicateQueryService.getListPage(record);
    }
    /**
     * 导出excel
     * @param record
     * @throws Exception
     */
    @RequestMapping("/export")
    public AjaxResponse<JSONObject> export(@RequestBody BaseInput record, HttpServletResponse response) throws Exception {
        return  duplicateQueryService.export(response,record);
    }
}
