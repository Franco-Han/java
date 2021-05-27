package com.ryxt.service;

import com.ryxt.entity.StatisticsSetting;
import com.ryxt.entity.SystemInfo;

public interface StatisticsSettingService {
    StatisticsSetting saveOrUpdate(StatisticsSetting record);

    StatisticsSetting select();

    StatisticsSetting selectByUserId(String userId);
}
