package com.ryxt.service;

import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckResult;
import com.ryxt.util.CommonListResponse;

import java.util.List;

public interface CheckResultService {

    CommonListResponse<CheckResult> getListPage(BaseInput user);

    CheckResult saveOrUpdate(CheckResult user);

    int deleteById(String id);

    CheckResult selectById(String id);

    String downloadImages(BaseInput record);
}
