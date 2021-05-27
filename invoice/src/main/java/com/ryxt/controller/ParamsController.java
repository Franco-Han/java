package com.ryxt.controller;

import com.ryxt.entity.BaseInput;
import com.ryxt.entity.Params;
import com.ryxt.entity.ParamsMy;
import com.ryxt.service.ParamsService;
import com.ryxt.util.AjaxListResponse;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/params")
public class ParamsController {

    @Autowired
    private ParamsService paramsservice;
    /**
    * 获取列表 分页
    * @param record
    * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.BaseInput>
    * @throws Exception
    */
    
    @RequestMapping("/getListPage")
    public CommonListResponse<Params> getListPage(@RequestBody BaseInput record) {
        return paramsservice.getListPage(record);
    }

    /**
    * 保存或更新
    * @param record
    * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.Params>
    * @throws Exception
    */
    
    @RequestMapping("/saveOrUpdate")
    public AjaxResponse<Params> saveOrUpdate(@RequestBody Params record) {
        return AjaxResponse.success(paramsservice.saveOrUpdate(record));
    }

    /**
    * 查看详细
    * @param id
    * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.Params>
    * @throws Exception
    */
    
    @GetMapping("/selectById/{id}")
    public AjaxResponse<Params> selectById(@PathVariable(value = "id") String id) {
        Params record = paramsservice.selectById(id);
        return AjaxResponse.success(record);
    }
    /**
    * 删除
    * @param id
    * @return com.ryxt.util.AjaxResponse<java.lang.String>
    * @throws Exception
    */
    
    @DeleteMapping("/deleteById/{id}")
    public AjaxResponse<String> deleteById(@PathVariable(value = "id") String id) {
        int count = paramsservice.deleteById(id);
        return AjaxResponse.success(count>0?"删除成功":"删除失败");
    }

    /**
     * 获取列表 分页
     * @param record
     * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.ParamsMy>
     * @throws Exception
     */

    @RequestMapping("/getMyListPage")
    public AjaxListResponse<ParamsMy> getMyListPage(@RequestBody ParamsMy record) {
        return paramsservice.getMyListPage(record);
    }

    /**
     * 保存或更新
     * @param ids
     * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.ParamsMy>
     * @throws Exception
     */

    @RequestMapping("/saveMyParams")
    public AjaxListResponse<ParamsMy> saveMyParams(@RequestBody List<ParamsMy> ids) {
        return paramsservice.saveMyParams(ids);
    }
}
