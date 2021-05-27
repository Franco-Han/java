package com.ryxt.controller;


import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.CheckResult;
import com.ryxt.service.CheckResultService;
import com.ryxt.service.CheckService;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
* @Description: 查验
* @Author: uenpeng
* @Date: 2020/10/12
*/
@RestController
@RequestMapping(value = "checkResult")
public class CheckResultController {

    @Autowired
    private CheckResultService checkService;
    /**
     * 获取列表 分页
     * @param record
     * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */

    @RequestMapping("/getListPage")
    public CommonListResponse<CheckResult> getListPage(@RequestBody BaseInput record) {
        return checkService.getListPage(record);
    }
    /**
     * 下载图片资源 分页
     * @param record
     * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */

    @RequestMapping("/downloadImages")
    public AjaxResponse<String> downloadImages(@RequestBody BaseInput record) {
        return AjaxResponse.success(checkService.downloadImages(record));
    }
}
