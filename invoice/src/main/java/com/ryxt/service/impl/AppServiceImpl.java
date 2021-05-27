package com.ryxt.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.AppVersion;
import com.ryxt.mapper.AppMapper;
import com.ryxt.service.AppService;
import com.ryxt.util.BaseAuthUtil;
import com.ryxt.util.BeanTrans;
import com.ryxt.util.CommonListResponse;
import com.ryxt.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppServiceImpl implements AppService {

	@Autowired
	public AppMapper appMapper;

    @Override
    public CommonListResponse<AppVersion> getListPage(BaseInput record) {
        CommonListResponse<AppVersion> commonListResponse = new CommonListResponse<AppVersion>(record);
        Page<AppVersion> page = new Page<AppVersion>(record.getPage(),record.getPageSize());
        EntityWrapper<AppVersion> wrapper =  (EntityWrapper<AppVersion>) BeanTrans.beanToNormal(record,AppVersion.class);
        wrapper.orderBy("create_date",true);
        commonListResponse.setTotalCount(appMapper.selectCount(wrapper));
        List<AppVersion> list = appMapper.selectPage(page,wrapper);
        commonListResponse.setList(list);
        return commonListResponse;
    }

    @Override
    public AppVersion saveOrUpdate(AppVersion record) {

        String userId = BaseAuthUtil.getCurrentUserId();

        if(StringUtil.isNullEmpty(record.getId())){
            record.setId(StringUtil.generateUUID());
        }
        AppVersion record1 = appMapper.selectById(record.getId());

        if(record1!=null){
            appMapper.updateById(record);
        }else {
            record.setCreatorId(userId);
            record.setCreateDate(new Date());
            appMapper.insert(record);
        }
        return record;
    }

    @Override
    public int deleteById(String id) {
        AppVersion sysAppVersion = new AppVersion();
        sysAppVersion.setId(id);
        return appMapper.updateById(sysAppVersion);
    }

    @Override
    public AppVersion selectById(String id) {
        return appMapper.selectById(id);
    }

    @Override
    public AppVersion getLastVersion() {
        CommonListResponse<AppVersion> commonListResponse = new CommonListResponse<AppVersion>(new BaseInput());
        EntityWrapper<AppVersion> wrapper =  (EntityWrapper<AppVersion>) BeanTrans.beanToNormal(new BaseInput(),AppVersion.class);
        wrapper.orderBy("create_date",false);
        commonListResponse.setTotalCount(appMapper.selectCount(wrapper));
        List<AppVersion> list = appMapper.selectList(wrapper);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }
}
