package com.ryxt.controller;

import com.ryxt.entity.Params;
import com.ryxt.entity.ParamsMy;
import com.ryxt.entity.StatisticsSetting;
import com.ryxt.service.ParamsService;
import com.ryxt.service.StatisticsSettingService;
import com.ryxt.util.AjaxListResponse;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
* @Description: 统计表生效设置
* @Author: uenpeng
* @Date: 2020/10/28
*/
@RestController
@RequestMapping(value = "/statistics_setting")
public class StatisticsSettingController {

    @Autowired
    private StatisticsSettingService service;

    /**
    * 保存或更新
    * @param record
    * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.StatisticsSetting>
    * @throws Exception
    */
    
    @RequestMapping("/saveOrUpdate")
    public AjaxResponse<StatisticsSetting> saveOrUpdate(@RequestBody StatisticsSetting record) {
        return AjaxResponse.success(service.saveOrUpdate(record));
    }

    /**
    * 查看详细
    * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.StatisticsSetting>
    * @throws Exception
    */
    
    @GetMapping("/select")
    public AjaxResponse<StatisticsSetting> select() {
        StatisticsSetting record = service.select();
        return AjaxResponse.success(record);
    }
}
