package com.ryxt.service.impl;

import com.ryxt.entity.StatisticsSetting;
import com.ryxt.mapper.StatisticsSettingMapper;
import com.ryxt.service.StatisticsSettingService;
import com.ryxt.util.BaseAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsSettingServiceImpl implements StatisticsSettingService {

	@Autowired
	public StatisticsSettingMapper statisticsSettingMapper;

    @Override
    public StatisticsSetting saveOrUpdate(StatisticsSetting record) {

        String userId = BaseAuthUtil.getCurrentUserId();

            StatisticsSetting record1 = statisticsSettingMapper.selectById(userId);
        if(record1!=null){
            record.setUserId(userId);
            statisticsSettingMapper.updateById(record);
        }else {
            record.setUserId(userId);
            statisticsSettingMapper.insert(record);
        }
        return record;
    }

    @Override
    public StatisticsSetting select() {
        String userId = BaseAuthUtil.getCurrentUserId();
        StatisticsSetting record =  statisticsSettingMapper.selectById(userId);
        if(record==null){
            record.setUserId(userId);
            statisticsSettingMapper.insert(record);
            record =  statisticsSettingMapper.selectById(userId);
        }
        return record;
    }
    @Override
    public StatisticsSetting selectByUserId(String userId) {
        StatisticsSetting record =  statisticsSettingMapper.selectById(userId);
        if(record==null){
            record.setUserId(userId);
            statisticsSettingMapper.insert(record);
            record =  statisticsSettingMapper.selectById(userId);
        }
        return record;
    }
}
