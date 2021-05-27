package com.ryxt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.*;
import com.ryxt.mapper.CheckListMapper;
import com.ryxt.mapper.CheckResultMapper;
import com.ryxt.mapper.InvoiceDateDiscrepancyMapper;
import com.ryxt.mapper.InvoiceDateMapper;
import com.ryxt.service.InvoiceDateDiscrepancyService;
import com.ryxt.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * 开票时间不符统计表
 */
@Service
public class InvoiceDateDiscrepancyImpl implements InvoiceDateDiscrepancyService {
    @Autowired
    private InvoiceDateDiscrepancyMapper invoiceDateDiscrepancyMapper;

    @Autowired
    private InvoiceDateMapper invoiceDateMapper;
    @Autowired
    private CheckResultMapper checkResultMapper;
    @Autowired
    private CheckListMapper checklistMapper;
    @Override
    public CommonListResponse<CheckList> getListPage(BaseInput record) {
        String userId = BaseAuthUtil.getCurrentUserId();
        CommonListResponse<CheckList> commonListResponse = new CommonListResponse<CheckList>(record);
        Page<CheckList> page = new Page<CheckList>(record.getPage(),record.getPageSize());
        EntityWrapper<InvoiceDate> datewrapper = new EntityWrapper();
        datewrapper.eq("user_id",  userId );
        datewrapper.eq("delete_flag",  "0" );
        Date startDate = null;
        Date endDate =null;
        List<InvoiceDate> result =invoiceDateMapper.selectList(datewrapper);
        if(result != null && result.size()>0){
            for (InvoiceDate invoiceDate:result) {
                startDate= invoiceDate.getStartDate();
                endDate = invoiceDate.getEndDate();
            }
            EntityWrapper<CheckList> wrapper =  (EntityWrapper<CheckList>) BeanTrans.beanToNormal(record,InvoiceDateDiscrepancy.class);
            wrapper.eq("user_id",  userId );
            wrapper.notBetween("invoice_date",startDate,endDate);
            wrapper.eq("result","真票");
            commonListResponse.setTotalCount(checklistMapper.selectCount(wrapper));
            List<CheckList> list =   checklistMapper.selectPage(page,wrapper);
            commonListResponse.setList(list);
        }
        return commonListResponse;
    }

    @Override
    public InvoiceDateDiscrepancy saveOrUpdate(InvoiceDateDiscrepancy record) {
        if(StringUtil.isNullEmpty(record.getId())){
            record.setId(StringUtil.generateUUID());
        }
        invoiceDateDiscrepancyMapper.insert(record);
        return record;
    }

    @Override
    public AjaxResponse<JSONObject> export(HttpServletResponse response, BaseInput record) throws Exception {
        String userId = BaseAuthUtil.getCurrentUserId();
        CommonListResponse<CheckList> commonListResponse = new CommonListResponse<CheckList>(record);
        EntityWrapper<InvoiceDate> datewrapper = new EntityWrapper();
        datewrapper.eq("user_id",  userId );
        datewrapper.eq("delete_flag",  "0" );
        Date startDate = null;
        Date endDate =null;
        List<InvoiceDate> result =invoiceDateMapper.selectList(datewrapper);
        List<CheckList> list = new ArrayList<>();
        if(result != null && result.size()>0){
            for (InvoiceDate invoiceDate : result) {
                startDate = invoiceDate.getStartDate();
                endDate = invoiceDate.getEndDate();
            }
            EntityWrapper<CheckList> wrapper = (EntityWrapper<CheckList>) BeanTrans.beanToNormal(record, InvoiceDateDiscrepancy.class);
            wrapper.eq("user_id", userId);
            wrapper.notBetween("invoice_date", startDate, endDate);
            wrapper.eq("result","真票");
            commonListResponse.setTotalCount(checklistMapper.selectCount(wrapper));
            list = checklistMapper.selectList(wrapper);
        }
            return AjaxResponse.success(ExcelUtils.DownloadUrl(list, InvoiceDateDiscrepancy.class, "开票日期不符统计表", record));
    }
}
