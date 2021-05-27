package com.ryxt.service;

import com.ryxt.entity.BaseInput;
import com.ryxt.entity.Company;
import com.ryxt.entity.User;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;

import java.util.List;

/**
* @Description: 购买方/销售方信息不符统计表
* @Author: uenpeng
* @Date: 2020/11/27
*/
public interface CompanyService {

    CommonListResponse<Company> getListPage(BaseInput record);

    Boolean saveOrUpdate(Company record);

    int deleteById(String id);
    int delete(List<Company> companies);

    Company selectById(String id);
}
