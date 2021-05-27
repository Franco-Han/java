package com.ryxt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.*;
import com.ryxt.mapper.CheckResultMapper;
import com.ryxt.mapper.DuplicateQueryMapper;
import com.ryxt.service.DuplicateQueryService;
import com.ryxt.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 重复查询统计实现类
 */
@Service
public class DuplicateQueryImpl implements DuplicateQueryService {
    @Autowired
    private DuplicateQueryMapper duplicateQueryMapper;
    @Autowired
    private CheckResultMapper checkResultMapper;
    @Override

    public CommonListResponse<DuplicateQuery> getListPage(BaseInput record) {
        String userId = BaseAuthUtil.getCurrentUserId();
        CommonListResponse<DuplicateQuery> commonListResponse = new CommonListResponse<DuplicateQuery>(record);
        Integer page = record.getPage();
        Integer pageSize = record.getPageSize();
        Map map = new HashMap();
        SectionUtil sectionUtil = new SectionUtil();
        if(record.getOptions().size()>0){
            map=sectionUtil.analysisSection(record);
        }
        map.put("page",(page-1)*pageSize);
        map.put("pageSize",pageSize);
        map.put("userId",userId);
        Integer count = duplicateQueryMapper.getListCount(map);
        commonListResponse.setTotalCount(count);
        List<DuplicateQuery> list = duplicateQueryMapper.getListPage(map);
        commonListResponse.setList(list);
        return commonListResponse;
    }

    @Override
    public DuplicateQuery saveOrUpdate(DuplicateQuery record) {
            if(StringUtil.isNullEmpty(record.getId())){
                record.setId(StringUtil.generateUUID());
            }
            duplicateQueryMapper.insert(record);
            return record;
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

        List<DuplicateQuery> list = duplicateQueryMapper.getListPage(map);
        return AjaxResponse.success( ExcelUtils.DownloadUrl(list,DuplicateQuery.class,"重复查询统计表",record));
    }
}
