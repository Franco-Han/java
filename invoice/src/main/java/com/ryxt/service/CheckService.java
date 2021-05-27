package com.ryxt.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.CheckListOcrVo;
import com.ryxt.entity.CheckResult;
import com.ryxt.util.AjaxResponse;
import com.ryxt.util.CommonListResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CheckService {

    CommonListResponse<CheckListOcrVo> getListVo(BaseInput record );
    CommonListResponse<CheckList> getListPage(BaseInput user);
    Map<String,Integer> findHandleCount();
    CommonListResponse<CheckList> getListPageHistory(BaseInput user);
    AjaxResponse<JSONObject> exportTemplate (HttpServletResponse response, BaseInput user) throws Exception;
    AjaxResponse<List<CheckList>>   uploadTemplate (MultipartFile file) throws Exception;
    AjaxResponse<JSONObject> export(HttpServletResponse response, BaseInput user) throws Exception;
    AjaxResponse<JSONObject> exportVo(HttpServletResponse response, BaseInput user) throws Exception;
    AjaxResponse<JSONObject> scanningExport(List<CheckList> checkLists ,  String code) throws Exception;
    AjaxResponse<Set> urlListExport(List<CheckList> checkLists , String code) throws Exception;
    CheckList saveOrUpdate(CheckList user);
    CheckList update(CheckList user);
    AjaxResponse<JSONObject> batchInsert(List<CheckList> checkLists);
    int deleteDeptById(String id);

    CheckList selectById(String id);

    CheckList getLastTask(List<CheckList> list);


    Object callback(CheckResult record);

    void isCompare(CheckList afterRecord, CheckResult record);

    void isDuplicate(CheckResult record);

    void isChange(CheckList afterRecord,CheckResult record);
    void isCompany( CheckResult record);
    void isDatematch(  CheckResult record);
    void isOneseller(CheckResult record);

    int batchDelete(List<CheckList> checkLists);
}
