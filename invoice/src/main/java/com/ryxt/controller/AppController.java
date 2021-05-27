package com.ryxt.controller;


import com.ryxt.entity.AppVersion;
import com.ryxt.entity.Dept;
import com.ryxt.service.AppService;
import com.ryxt.service.CheckService;
import com.ryxt.service.DeptService;
import com.ryxt.util.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @Description: app版本控制
* @Author: uenpeng
* @Date: 2020/11/3
*/
@RestController
@RequestMapping(value = "app")
public class AppController {

    @Autowired
    private AppService appService;
    @GetMapping("/getLastVersion")
    public AjaxResponse<AppVersion> getLastVersion() {
        return AjaxResponse.success(appService.getLastVersion());
    }
}
