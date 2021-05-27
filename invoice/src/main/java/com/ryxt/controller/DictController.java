package com.ryxt.controller;

import com.ryxt.entity.BaseInput;
import com.ryxt.entity.SysDict;
import com.ryxt.service.DeptService;
import com.ryxt.service.DictService;
import com.ryxt.util.AjaxListResponse;
import com.ryxt.util.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
* @Description: 数据字典
* @Author: uenpeng
* @Date: 2020/10/10
*/
@RestController
@RequestMapping(value = "dict")
public class DictController {


    @Autowired
    private DictService dictService;
    /**
    *  获取字典列表
    * @param sysDict
    * @return com.ryxt.util.AjaxListResponse<com.ryxt.entity.SysDict>
    * @throws Exception
    */
    
    @RequestMapping("/getDictList")
    public AjaxListResponse<SysDict> getDictList(SysDict sysDict) {
        return AjaxListResponse.success(dictService.getDictList(sysDict));
    }
    /**
     *  获取字典列表 分页
     * @param record
     * @return com.ryxt.util.AjaxListResponse<com.ryxt.entity.BaseInput>
     * @throws Exception
     */

    @RequestMapping("/getListPage")
    public CommonListResponse<SysDict> getDictListPage(BaseInput record) {
        return dictService.getListPage(record);
    }
}
