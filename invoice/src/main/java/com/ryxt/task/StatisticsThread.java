package com.ryxt.task;

import com.ryxt.entity.CheckList;
import com.ryxt.entity.CheckResult;
import com.ryxt.entity.StatisticsSetting;
import com.ryxt.mapper.CheckListMapper;
import com.ryxt.service.CheckService;
import com.ryxt.service.ExecuteService;
import com.ryxt.service.ParamsService;
import com.ryxt.service.StatisticsSettingService;
import com.ryxt.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class StatisticsThread implements Runnable{

    CheckList afterRecord;
    CheckResult checkResult;
    /**
     * 构造函数
     * @param afterRecord,checkResult
     */
    public StatisticsThread(CheckList afterRecord,CheckResult checkResult) {
        this.afterRecord = afterRecord;
        this.checkResult = checkResult;
    }

    @Override
    public void run() {
        try {
            StatisticsSettingService statisticsSettingService  = (StatisticsSettingService) SpringContextUtil.getBean("statisticsSettingServiceImpl");
            // 统计分析条件
            StatisticsSetting statisticsSetting = statisticsSettingService.selectByUserId(afterRecord.getUserId());
            CheckService checkService = (CheckService) SpringContextUtil.getBean("checkServiceImpl");
            // 信息对比统计表
            if(statisticsSetting.isCompare()){
                checkService.isCompare(afterRecord,checkResult);
            }
//            // 重复查询统计表
//            if(statisticsSetting.isDuplicate()){
//                checkService.isDuplicate(checkResult);
//            }
            // 复查结果变更统计表
            if(statisticsSetting.isChange()){
                checkService.isChange(afterRecord,checkResult);
            }
            // 购买方/销售方信息不符统计表
//            if(statisticsSetting.isCompany()){
//                checkService.isCompany(checkResult);
//            }
            // 开票日期不符统计表
//            if(statisticsSetting.isDatematch()){
//                checkService.isDatematch(checkResult);
//            } // 同一销售方统计表
//            if(statisticsSetting.isOneseller()){
//                checkService.isOneseller(checkResult);
//            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
