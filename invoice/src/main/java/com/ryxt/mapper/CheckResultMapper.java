package com.ryxt.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.CheckResult;

import java.util.List;

/**
* @Description: CheckResult.java
* @Author: uenpeng
* @Date: 2020/10/28
*/
public interface CheckResultMapper extends BaseMapper<CheckResult> {
    CheckResult  findImmediately(CheckResult record);
    CheckResult  findLastTime(CheckResult record);
}
