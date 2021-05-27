package com.ryxt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.ReviewResultChange;
import com.ryxt.mapper.ReviewResultChangeMapper;
import com.ryxt.service.ReviewResultChangeService;
import com.ryxt.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 * 复查结果变更统计表
 */
@Service
public class ReviewResultChangeImpl implements ReviewResultChangeService {
    @Autowired
    private  ReviewResultChangeMapper reviewResultChangeMapper;
    @Override
    public CommonListResponse<ReviewResultChange> getListPage(BaseInput record) {
        String userId = BaseAuthUtil.getCurrentUserId();
        CommonListResponse<ReviewResultChange> commonListResponse = new CommonListResponse<ReviewResultChange>(record);
        Page<ReviewResultChange> page = new Page<ReviewResultChange>(record.getPage(),record.getPageSize());
        EntityWrapper<ReviewResultChange> wrapper =  (EntityWrapper<ReviewResultChange>) BeanTrans.beanToNormal(record,ReviewResultChange.class);
        wrapper.eq("user_id",  userId );
        commonListResponse.setTotalCount(reviewResultChangeMapper.selectCount(wrapper));
        List<ReviewResultChange> list = reviewResultChangeMapper.selectPage(page,wrapper);
        commonListResponse.setList(list);
        return commonListResponse;
    }

    @Override
    public ReviewResultChange saveOrUpdate(ReviewResultChange record) {
        if(StringUtil.isNullEmpty(record.getId())){
            record.setId(StringUtil.generateUUID());
        }
        reviewResultChangeMapper.insert(record);
        return record;
    }

    @Override
    public AjaxResponse<JSONObject> export(HttpServletResponse response, BaseInput record) throws Exception {
        String userId = BaseAuthUtil.getCurrentUserId();
        EntityWrapper<ReviewResultChange> wrapper =  (EntityWrapper<ReviewResultChange>) BeanTrans.beanToNormal(record,ReviewResultChange.class);
        wrapper.eq("user_id",  userId );
        List<ReviewResultChange> list = reviewResultChangeMapper.selectList(wrapper);
        return AjaxResponse.success( ExcelUtils.DownloadUrl(list,ReviewResultChange.class,"复查结果变更统计表",record));
    }

}
