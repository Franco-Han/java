package com.ryxt.controller;


import com.ryxt.entity.CheckList;
import com.ryxt.entity.ReCheckTask;
import com.ryxt.service.CheckService;
import com.ryxt.service.ReCheckTaskService;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
* @Description: 复查任务
* @Author: uenpeng
* @Date: 2020/10/16
*/
@RestController
@RequestMapping(value = "reCheck")
public class ReCheckTaskController {

    @Autowired
    private ReCheckTaskService reCheckTaskService;

    /**
     * 创建复查任务
     * @param record
     * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.ReCheckTask>
     * @throws Exception
     */

    @RequestMapping("/saveOrUpdate")
    public AjaxResponse<ReCheckTask> saveOrUpdate(@RequestBody ReCheckTask record) {
        if(record.getCronDate().getTime()<=(System.currentTimeMillis()+60*60*1000)){
            return AjaxResponse.fail("复查任务创建失败，请选择最少一个小时之后复查",null);
        }
        return AjaxResponse.success(reCheckTaskService.saveOrUpdate(record));
    }
    /**
     * 立即复查
     * @param record
     * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.ReCheckTask>
     * @throws Exception
     */
    @RequestMapping("/reCheckRightNow")
    public AjaxResponse<ReCheckTask> reCheckRightNow(@RequestBody ReCheckTask record) {
        return AjaxResponse.success(reCheckTaskService.start(record));
    }
    /**
     * 查看详细
     * @param id
     * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */

    @GetMapping("/selectById/{id}")
    public AjaxResponse<ReCheckTask> selectById(@PathVariable(value = "id") String id) {
        ReCheckTask record = reCheckTaskService.selectById(id);
        return AjaxResponse.success(record);
    }
}
