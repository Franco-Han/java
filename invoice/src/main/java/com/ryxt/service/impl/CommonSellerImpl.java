package com.ryxt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.*;
import com.ryxt.mapper.CheckListMapper;
import com.ryxt.mapper.CheckResultMapper;
import com.ryxt.mapper.CommonSellerMapper;
import com.ryxt.service.CommonSellerService;
import com.ryxt.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同一销售方统计表
 */
@Service
public class CommonSellerImpl implements CommonSellerService {
    @Autowired
    private CommonSellerMapper commonSellerMapper;

    @Override
    public CommonListResponse<CommonSeller> getListPage(BaseInput record) {
        String userId = BaseAuthUtil.getCurrentUserId();
        CommonListResponse<CommonSeller> commonListResponse = new CommonListResponse<CommonSeller>(record);
        Map map = new HashMap();
        Integer page = record.getPage();
        Integer pageSize = record.getPageSize();
        SectionUtil sectionUtil = new SectionUtil();
        if(record.getOptions().size()>0){
            map=sectionUtil.analysisSection(record);
        }
        map.put("userId",userId);
        map.put("page",(page-1)*pageSize);
        map.put("pageSize",pageSize);
        commonListResponse.setTotalCount(commonSellerMapper.findCount(map));
        List<CommonSeller> list = commonSellerMapper.getListPage(map);
        commonListResponse.setList(list);
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
        List<CommonSeller> list = commonSellerMapper.getListPage(map);
        return AjaxResponse.success( ExcelUtils.DownloadUrl(list,CommonSeller.class,"同一销售方统计表",record));
    }

    @Override
    public CommonSeller saveOrUpdate(CommonSeller record) {

        String userId = BaseAuthUtil.getCurrentUserId();

        if(StringUtil.isNullEmpty(record.getId())){
            record.setId(StringUtil.generateUUID());
        }
        CommonSeller record1 = commonSellerMapper.selectById(record.getId());

            commonSellerMapper.insert(record);
        return record;
    }
}
