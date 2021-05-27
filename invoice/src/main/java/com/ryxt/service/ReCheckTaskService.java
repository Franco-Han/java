package com.ryxt.service;

import com.ryxt.entity.CheckList;
import com.ryxt.entity.ReCheckTask;
import com.ryxt.util.CommonListResponse;

import java.util.List;

public interface ReCheckTaskService {
    ReCheckTask saveOrUpdate(ReCheckTask user);
    ReCheckTask selectById(String id);

    ReCheckTask start(ReCheckTask t);
}
