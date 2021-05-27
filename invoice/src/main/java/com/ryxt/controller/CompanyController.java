package com.ryxt.controller;

import com.ryxt.entity.BaseInput;
import com.ryxt.entity.Company;
import com.ryxt.service.CompanyService;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    /**
    * 获取列表 分页
    * @param record
    * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.BaseInput>
    * @throws Exception
    */
    
    @RequestMapping("/getListPage")
    public CommonListResponse<Company> getListPage(@RequestBody BaseInput record) {
        return companyService.getListPage(record);
    }

    /**
    * 保存或更新
    * @param record
    * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.Company>
    * @throws Exception
    */
    
    @RequestMapping("/saveOrUpdate")
    public AjaxResponse saveOrUpdate(@RequestBody Company record) {
        Boolean a = companyService.saveOrUpdate(record);
        return AjaxResponse.success(a);
    }

    /**
    * 查看详细
    * @param id
    * @return com.ryxt.util.AjaxResponse<com.ryxt.entity.Company>
    * @throws Exception
    */
    
    @GetMapping("/selectById/{id}")
    public AjaxResponse<Company> selectById(@PathVariable(value = "id") String id) {
        Company record = companyService.selectById(id);
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
        int count = companyService.deleteById(id);
        return AjaxResponse.success(count>0?"删除成功":"删除失败");
    }
    @PostMapping("/delete")
    public AjaxResponse<String> deleteByIds(@RequestBody List<Company> companies) {
        int count = companyService.delete(companies);
        return AjaxResponse.success(count>0?"删除成功":"删除失败");
    }
}
