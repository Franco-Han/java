package com.ryxt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.*;
import com.ryxt.service.CommonSellerService;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 同一销售方统计表
 */
@RestController
@RequestMapping("csc")
public class CommonSellerController {
    @Autowired
    private CommonSellerService commonSellerService;

    /**
     * 获取列表 分页
     * @param record
     * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */
    @RequestMapping("/getListPage")
    public CommonListResponse<CommonSeller> getListPage(@RequestBody BaseInput record) {
        return commonSellerService.getListPage(record);
    }
    /**
     * 导出excel
     * @param record
     * @throws Exception
     */
    @RequestMapping("/export")
    public AjaxResponse<JSONObject> export(@RequestBody BaseInput record, HttpServletResponse response) throws Exception {
        return  commonSellerService.export(response,record);
    }
}
