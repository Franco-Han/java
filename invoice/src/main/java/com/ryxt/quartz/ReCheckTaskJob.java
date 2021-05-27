package com.ryxt.quartz;

import com.alibaba.fastjson.JSON;
import com.ryxt.entity.ReCheckTask;
import com.ryxt.service.ReCheckTaskService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReCheckTaskJob implements Job {
    private Logger logger = LoggerFactory.getLogger(ReCheckTaskJob.class);

    @Autowired
    private ReCheckTaskService reCheckTaskService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("RemindTask task start execute.{}",jobExecutionContext.getMergedJobDataMap().get("task"));
        ReCheckTask t = JSON.parseObject(jobExecutionContext.getMergedJobDataMap().get("task").toString(), ReCheckTask.class);
       if(t!=null){
           reCheckTaskService.start(t);
       }
    }
}
