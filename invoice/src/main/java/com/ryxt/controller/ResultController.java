package com.ryxt.controller;


import com.ryxt.entity.CheckResult;
import com.ryxt.service.CheckService;
import com.ryxt.util.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @Description: 查验结果
* @Author: uenpeng
* @Date: 2020/11/05
*/
@RestController
@RequestMapping(value = "result")
public class ResultController {

    @Autowired
    private CheckService checkService;

    @RequestMapping("/callback")
    public AjaxResponse<Object> execute(@RequestBody CheckResult record) {

        return AjaxResponse.success(checkService.callback(record));
    }
}
