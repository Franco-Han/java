package com.ryxt.controller;

import com.ryxt.entity.SystemInfo;
import com.ryxt.service.SystemService;
import com.ryxt.util.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @Description: 系统设置
* @Author: uenpeng
* @Date: 2020/10/28
*/
@RestController
@RequestMapping(value = "/system")
public class SystemController {

    @Autowired
    private SystemService service;

    /**
    * 保存或更新
    * @param record
    * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.SystemInfo>
    * @throws Exception
    */
    
    @RequestMapping("/saveOrUpdate")
    public AjaxResponse<SystemInfo> saveOrUpdate(@RequestBody SystemInfo record) {
        return AjaxResponse.success(service.saveOrUpdate(record));
    }

    /**
    * 查看详细
    * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.SystemInfo>
    * @throws Exception
    */
    
    @GetMapping("/select")
    public AjaxResponse<SystemInfo> select() {
        SystemInfo record = service.select();
        return AjaxResponse.success(record);
    }
}
