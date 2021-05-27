package com.ryxt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.*;
import com.ryxt.mapper.CompanyMapper;
import com.ryxt.mapper.InformationDiscrepancyMapper;
import com.ryxt.service.InformationDiscrepancyService;
import com.ryxt.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购买方/销售方不符统计表
 */
@Service
public class InformationDiscrepancyImpl implements InformationDiscrepancyService {
    @Autowired
    private InformationDiscrepancyMapper informationDiscrepancyMapper;

    @Autowired
    public CompanyMapper companyMapper;

    @Override
    public InformationDiscrepancy saveOrUpdate(InformationDiscrepancy record) {
        if(StringUtil.isNullEmpty(record.getId())){
            record.setId(StringUtil.generateUUID());
        }
        informationDiscrepancyMapper.insert(record);
        return record;
    }

    public List<Company> getCompany(String userId){
        EntityWrapper<Company> wrapper = new EntityWrapper<Company>();
        wrapper.eq("user_id",  userId );
        wrapper.eq("delete_flag",  "0" );
        wrapper.eq("status",  "1" );
        List<Company> list = companyMapper.selectList(wrapper);
        return list;
    }
    @Override
    @Transactional
    public CommonListResponse<InformationDiscrepancy> getListPage(BaseInput record) {
        CommonListResponse<InformationDiscrepancy> commonListResponse = new CommonListResponse<InformationDiscrepancy>(record);
        String userId = BaseAuthUtil.getCurrentUserId();
        List<Company> listCompany=getCompany(userId);
        if(listCompany != null && listCompany.size()>0){
            Integer page = record.getPage();
            Integer pageSize = record.getPageSize();
            Map map = new HashMap();
            String field="";
            String value="";
            String startTime="";
            String endTime="";
            SectionUtil sectionUtil = new SectionUtil();
            if(record.getOptions().size()>0){
                map=sectionUtil.analysisSection(record);
            }
            map.put("page",(page-1)*pageSize);
            map.put("pageSize",pageSize);
            map.put("userId",userId);
            List<InformationDiscrepancy> list = informationDiscrepancyMapper.getListPage(map);
            commonListResponse.setList(list);
            commonListResponse.setTotalCount(informationDiscrepancyMapper.getListCount(map));
        }
        return commonListResponse;
    }

    @Override
    public AjaxResponse<JSONObject> export(HttpServletResponse response, BaseInput record) throws Exception {
        String userId = BaseAuthUtil.getCurrentUserId();
            Map map = new HashMap();
            SectionUtil sectionUtil = new SectionUtil();
            if(record.getOptions().size()>0){
                map=sectionUtil.analysisSection(record);
            }
            map.put("userId",userId);
            List<InformationDiscrepancy> list = informationDiscrepancyMapper.getListPage(map);
            return AjaxResponse.success( ExcelUtils.DownloadUrl(list,InformationDiscrepancy.class,"购买方销售方信息不符统计表",record));

    }
}
