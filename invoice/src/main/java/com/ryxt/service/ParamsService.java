package com.ryxt.service;

import com.ryxt.entity.BaseInput;
import com.ryxt.entity.Company;
import com.ryxt.entity.Params;
import com.ryxt.entity.ParamsMy;
import com.ryxt.util.AjaxListResponse;
import com.ryxt.util.CommonListResponse;

import java.util.List;

public interface ParamsService {

    CommonListResponse<Params> getListPage(BaseInput record);

    Params saveOrUpdate(Params record);

    int deleteById(String id);

    Params selectById(String id);

    AjaxListResponse<ParamsMy> getMyListPage(ParamsMy record);

    AjaxListResponse<ParamsMy> saveMyParams(List<ParamsMy> ids);
}
