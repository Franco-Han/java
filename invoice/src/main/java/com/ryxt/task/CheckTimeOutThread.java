package com.ryxt.task;

import com.ryxt.entity.CheckList;
import com.ryxt.mapper.CheckListMapper;
import com.ryxt.service.ExecuteService;
import com.ryxt.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class CheckTimeOutThread implements Runnable{

    private Logger logger = LoggerFactory.getLogger(CheckTimeOutThread.class);
    @Autowired
    ExecuteService executeService;
    CheckList checkList;

    private  int expiredTime = 300000 ;
    /**
     * 构造函数
     * @param checkList
     */
    public CheckTimeOutThread(CheckList checkList, ExecuteService executeService) {
        this.checkList = checkList;
        this.executeService = executeService;
    }

    @Override
    public void run() {
        try {
            logger.info("===========CheckTimeOutThread 开始查验==========>>>"+checkList.getInvoiceNumber());
            CheckListMapper checkListMapper  = (CheckListMapper) SpringContextUtil.getBean("checkListMapper");
            //2分钟后如果还是没有查验结果,则重新执行
            CheckList checkResult = new CheckList();
            int min = expiredTime/60000;
            logger.info("当前时间:" + System.currentTimeMillis());
            logger.info("开始处理时间:" + checkList.getUpdateDate().getTime());
            logger.info("超时时间:" + expiredTime);
            logger.info("是否超时:" + ((System.currentTimeMillis() - checkList.getUpdateDate().getTime())> expiredTime));
            if((System.currentTimeMillis() - checkList.getUpdateDate().getTime())> expiredTime ){
                checkResult =  checkListMapper.selectById(checkList.getId());
                if("1".equals(checkResult.getStatus())) {
                    logger.info("===========超过"+min+"分钟了，查验失败,超时==========>>>" + checkList.getInvoiceNumber());
                    checkResult.setStatus("2");
                    checkResult.setResult("查验失败");
                    checkResult.setReason("查验超时");
                    checkResult.setUpdateDate(new Date());
                    checkListMapper.updateById(checkResult);
                }else {
                    logger.info("===========已经查验完成了==========>>>"+checkList.getInvoiceNumber());
                }
            }else{
                logger.info("==========="+min+"分钟内，继续等待==========>>>"+checkList.getInvoiceNumber());
            }
            logger.info("===========CheckTimeOutThread end==========>>>"+checkList.getInvoiceNumber());
        }catch (Exception e){

        }
    }
}
