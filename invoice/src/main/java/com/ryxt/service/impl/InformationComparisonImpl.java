package com.ryxt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.InformationComparison;
import com.ryxt.entity.InformationDiscrepancy;
import com.ryxt.entity.ReviewResultChange;
import com.ryxt.mapper.InformationComparisonMapper;
import com.ryxt.mapper.InformationDiscrepancyMapper;
import com.ryxt.service.InformationComparisonService;
import com.ryxt.service.InformationDiscrepancyService;
import com.ryxt.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 * 票面信息比对统计表
 */
@Service
public class InformationComparisonImpl implements InformationComparisonService {
    @Autowired
    private InformationComparisonMapper informationComparisonMapper;
    @Override
    public CommonListResponse<InformationComparison> getListPage(BaseInput record) {
        String userId = BaseAuthUtil.getCurrentUserId();
        CommonListResponse<InformationComparison> commonListResponse = new CommonListResponse<InformationComparison>(record);
        Page<InformationComparison> page = new Page<InformationComparison>(record.getPage(),record.getPageSize());
        EntityWrapper<InformationComparison> wrapper =  (EntityWrapper<InformationComparison>) BeanTrans.beanToNormal(record, InformationComparison.class);
        wrapper.eq("user_id",userId);
        commonListResponse.setTotalCount(informationComparisonMapper.selectCount(wrapper));
        List<InformationComparison> list = informationComparisonMapper.selectPage(page,wrapper);
        commonListResponse.setList(list);
        return commonListResponse;
    }

    @Override
    public InformationComparison saveOrUpdate(InformationComparison record) {
        if(StringUtil.isNullEmpty(record.getId())){
            record.setId(StringUtil.generateUUID());
        }
        informationComparisonMapper.insert(record);
        return record;
    }

    @Override
    public AjaxResponse<JSONObject> export(HttpServletResponse response, BaseInput record) throws Exception {
        String userId = BaseAuthUtil.getCurrentUserId();
        EntityWrapper<InformationComparison> wrapper =  (EntityWrapper<InformationComparison>) BeanTrans.beanToNormal(record,InformationComparison.class);
        wrapper.eq("user_id",userId);
        List<InformationComparison> list = informationComparisonMapper.selectList(wrapper);
        return AjaxResponse.success( ExcelUtils.DownloadUrl(list,InformationComparison.class,"票面信息对比统计表",record));
    }
}
