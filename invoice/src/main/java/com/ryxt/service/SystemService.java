package com.ryxt.service;

import com.ryxt.entity.SystemInfo;

public interface SystemService {
    SystemInfo saveOrUpdate(SystemInfo record);

    SystemInfo select();
}
