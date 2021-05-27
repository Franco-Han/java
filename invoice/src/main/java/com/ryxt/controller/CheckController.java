package com.ryxt.controller;


import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckListOcrVo;
import com.ryxt.entity.Dept;
import com.ryxt.entity.CheckList;
import com.ryxt.service.CheckService;
import com.ryxt.service.DeptService;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @Description: 查验
* @Author: uenpeng
* @Date: 2020/10/12
*/
@RestController
@RequestMapping(value = "check")
public class CheckController {

    @Autowired
    private CheckService checkService;
    /**
     * 获取列表 分页
     * @param record
     * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */

    @RequestMapping("/getListPage")
        public CommonListResponse<CheckList> getListPage(@RequestBody BaseInput record) {
        return checkService.getListPage(record);
    }
    /**
     * 获取列表 分页
     * @param record
     * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.CheckListOcrVo>
     * @throws Exception
     */
    @RequestMapping("/getListVo")
    public CommonListResponse<CheckListOcrVo> getListVo(@RequestBody BaseInput record){
        return  checkService.getListVo(record);
    }

    /**
     * 导出
     * @param record
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/exportVo")
    public AjaxResponse<JSONObject> exportVo(@RequestBody BaseInput record, HttpServletResponse response) throws Exception {
        return  checkService.exportVo(response,record);
    }
    /**
     * 获取列表 分页
     * @param record
     * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */

    @RequestMapping("/test")
    public String test(@RequestBody BaseInput record) {
        return "111111111111111";
    }
    /**
     * 获取列表 分页
     * @param record
     * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */

    @RequestMapping("/getListPageHistory")
    public CommonListResponse<CheckList> getListPageHistory(@RequestBody BaseInput record) {
        return checkService.getListPageHistory(record);
    }
    /**
     * 保存或更新
     * @param record
     * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */

    @RequestMapping("/saveOrUpdate")
    public AjaxResponse<CheckList> saveOrUpdate(@RequestBody CheckList record) {
        return AjaxResponse.success(checkService.saveOrUpdate(record));
    }
    /**
     * 编辑重新查询
     * @param record
     * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */
    @RequestMapping("/update")
    public AjaxResponse<CheckList> update(@RequestBody CheckList record) {
        return AjaxResponse.success(checkService.update(record));
    }
    /**
     * 批量插入
     */
    @RequestMapping("/batchInsert")
    public AjaxResponse batchInsert(@RequestBody List<CheckList> checkLists){
        return AjaxResponse.success(checkService.batchInsert(checkLists));
    }

    /**
     * 查看详细
     * @param id
     * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.CheckList>
     * @throws Exception
     */

    @GetMapping("/selectById/{id}")
    public AjaxResponse<CheckList> selectById(@PathVariable(value = "id") String id) {
        CheckList record = checkService.selectById(id);
        return AjaxResponse.success(record);
    }
    /**
     * 删除
     * @param id
     * @return com.ryxt.util.AjaxResponse<java.lang.String>
     * @throws Exception
     */

    @DeleteMapping("/deleteDeptById/{id}")
    public AjaxResponse<String> deleteDeptById(@PathVariable(value = "id") String id) {
        int count = checkService.deleteDeptById(id);
        return AjaxResponse.success(count>0?"删除成功":"删除失败");
    }
    /**
     * 批量删除
     * @return com.ryxt.util.AjaxResponse<java.lang.String>
     * @throws Exception
     */

    @PostMapping("/batchDelete")
    public AjaxResponse<String> batchDelete(@RequestBody List<CheckList> checkLists) {
        int count = checkService.batchDelete(checkLists);
        return AjaxResponse.success(count>0?"删除成功":"删除失败");
    }

    /**
     * 导出excel
     * @param record
     * @throws Exception
     */

    @RequestMapping("/export")
    public AjaxResponse<JSONObject> export(@RequestBody BaseInput record, HttpServletResponse response) throws Exception {
       return  checkService.export(response,record);
    }
    @RequestMapping("/exportTemplate")
    public AjaxResponse<JSONObject> exportTemplate (@RequestBody BaseInput record, HttpServletResponse response) throws Exception {
        return  checkService.exportTemplate(response,record);
    }
    @PostMapping("/uploadTemplate")
    public AjaxResponse<List<CheckList>>  uploadTemplate (MultipartFile file) throws Exception {
        return  checkService.uploadTemplate(file);
    }
    @RequestMapping("/scanningExport/{code}")
    public AjaxResponse<JSONObject> scanningExport(@RequestBody List<CheckList> checkLists,
                                                   @PathVariable(value = "code")String code) throws Exception {
        /**
         * code:
         * 0 为：下载扫描影像导出
         * 1 为：下载图片导出
         */
        return  checkService.scanningExport(checkLists ,code);
    }
    @RequestMapping("urlListExports/{code}")
    public AjaxResponse<Set> urlListExport(@RequestBody List<CheckList> checkLists,
                                           @PathVariable(value = "code")String code) throws Exception{
        return checkService.urlListExport(checkLists,code);
    }
    @RequestMapping("/findHandleCount")
    public AjaxResponse<Map<String,Integer>> findHandleCount(){
        return AjaxResponse.success(checkService.findHandleCount());
    }
}
