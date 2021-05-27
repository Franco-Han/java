package com.ryxt.service;


import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;

/**
* @Description: FileService.java
* @Author: uenpeng
* @Date: 2021年1月5日
*/

public interface FileService {
    /**
     * 获取列表
     * @param file
     * @return JSONObject
     * @throws Exception
     */
    JSONObject upload(File file) throws IOException;
}
