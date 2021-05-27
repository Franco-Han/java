package com.ryxt.service;


import com.ryxt.entity.BaseInput;
import com.ryxt.entity.SysDict;
import com.ryxt.util.CommonListResponse;

import java.util.List;

/**
* @Description: DictService.java
* @Author: uenpeng
* @Date: 2020/10/10
*/

public interface DictService {
    /**
     * 获取列表
     * @param sysDict
     * @return java.util.List<com.ryxt.entity.SysDict>
     * @throws Exception
     */
    List<SysDict> getDictList(SysDict sysDict);
    /**
     * 获取列表  分页
     * @param sysDict
     * @return com.ryxt.util.CommonListResponse<com.ryxt.entity.BaseInput>
     * @throws Exception
     */
    CommonListResponse<SysDict> getListPage(BaseInput sysDict);
}
