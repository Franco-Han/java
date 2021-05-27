package com.ryxt.service;

import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.OcrInfo;
import com.ryxt.util.CommonListResponse;

public interface OcrService {


    CommonListResponse<OcrInfo> getListPage(BaseInput user);

    OcrInfo saveOrUpdate(OcrInfo user);

    int deleteById(String id);

    OcrInfo selectById(String id);

    OcrInfo scan(String filePath);

    OcrInfo scan2(String filePath,String tempFile);
    OcrInfo scanNoSave(String filePath);
}
