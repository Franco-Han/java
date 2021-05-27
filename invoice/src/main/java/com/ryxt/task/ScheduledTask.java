package com.ryxt.task;

import com.ryxt.entity.CheckList;
import com.ryxt.service.ExecuteService;
import com.ryxt.service.TaskService;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

    @Component
@Profile(value = "online")
public class ScheduledTask {

    private Logger logger = LoggerFactory.getLogger(ScheduledTask.class);
    @Autowired
    private TaskService taskService;

    @Autowired
    ExecuteService executeService;
    /**
     * 每隔10秒执行, 单位：ms。
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void testFixRate() throws SchedulerException {
//        ThreadPoolTaskExecutor tpte = (ThreadPoolTaskExecutor) SpringContextUtil.getBean("taskExecutor");
        List<CheckList> list =  taskService.getTasks();
        logger.info("获取到未完成或需要处理的数据:"+list.size()+"条");
        for(CheckList checkList:list){

            String status="待处理";
            if("1".equals(checkList.getStatus())){
                status = "处理中";
            }
            logger.info("判断是否处理中：=》号码"+checkList.getInvoiceNumber()+"====>"+status);
            // 未执行的任务
            if("0".equals(checkList.getStatus())){
                //执行任务
                logger.info("开始处理==》号码"+checkList.getInvoiceNumber());
//                tpte.execute(new CheckThread(checkList,executeService));
                CheckThread t =  new CheckThread(checkList,executeService);
                t.run();
            }else{
                //执行任务
                logger.info("开始验证超时处理==》号码"+checkList.getInvoiceNumber());
//                tpte.execute(new CheckThread(checkList,executeService));
                CheckTimeOutThread t =  new CheckTimeOutThread(checkList,executeService);
                t.run();
            }
        }
    }


}
