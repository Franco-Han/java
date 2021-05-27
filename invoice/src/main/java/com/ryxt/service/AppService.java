package com.ryxt.service;

import com.ryxt.entity.AppVersion;
import com.ryxt.entity.BaseInput;
import com.ryxt.util.CommonListResponse;

public interface AppService {

    CommonListResponse<AppVersion> getListPage(BaseInput user);

    AppVersion saveOrUpdate(AppVersion user);

    int deleteById(String id);

    AppVersion selectById(String id);

    AppVersion getLastVersion();
}
