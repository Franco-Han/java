package com.ryxt.controller;


import com.ryxt.entity.Dept;
import com.ryxt.service.DeptService;
import com.ryxt.util.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "dept")
public class DeptController {

    @Autowired
    private DeptService deptService;
    @GetMapping("/selectById/{id}")
    public AjaxResponse<Dept> selectById(@PathVariable(value = "id") String id) {
        Dept dept = deptService.selectById(id);
        return AjaxResponse.success(dept);
    }
}
