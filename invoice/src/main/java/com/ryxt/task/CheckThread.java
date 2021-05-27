package com.ryxt.task;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.CheckList;
import com.ryxt.mapper.CheckListMapper;
import com.ryxt.quartz.ReCheckTaskJob;
import com.ryxt.service.ExecuteService;
import com.ryxt.util.SpringContextUtil;
import com.ryxt.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.Map;
import java.util.Random;

public class CheckThread implements Runnable{

    private Logger logger = LoggerFactory.getLogger(CheckThread.class);
    @Autowired
    ExecuteService executeService;
    CheckList checkList;
    /**
     * 构造函数
     * @param checkList
     */
//    public CheckThread( CheckList checkList) {
//        this.checkList = checkList;
//    }
    public CheckThread( CheckList checkList,ExecuteService executeService) {
        this.checkList = checkList;
        this.executeService = executeService;
    }

    @Override
    public void run() {
        try {
            logger.info("===========CheckThread 开始查验==========>>>"+checkList.getInvoiceNumber());
            CheckListMapper checkListMapper  = (CheckListMapper) SpringContextUtil.getBean("checkListMapper");
            if( StringUtil.isNullEmpty(checkList.getInvoiceNumber())
                    || StringUtil.isNullEmpty(checkList.getInvoiceCode())
                    || checkList.getInvoiceDate()==null
                    ||  StringUtil.isNullEmpty(checkList.getInvoiceType())
                    ||("01".equals(checkList.getInvoiceType()) && StringUtil.isNullEmpty(checkList.getExcludingTaxPrice()))
                    ||("10".equals(checkList.getInvoiceType()) &&  StringUtil.isNullEmpty(checkList.getCheckCode()))
                    ||("04".equals(checkList.getInvoiceType()) &&  StringUtil.isNullEmpty(checkList.getCheckCode()))
            ){
                checkList.setResult("查验失败");
                checkList.setStatus("2");
                checkList.setReason("参数错误");
                checkList.setUpdateDate(new Date());
                checkListMapper.updateById(checkList);
            }else {
                CheckList checkList2 = new CheckList();
                checkList2.setId(checkList.getId());
                checkList2.setStatus("1");
                Date now = new Date();
                checkList2.setUpdateDate(now);
                checkList2.setReCheck(checkList.getReCheck());
                checkListMapper.updateById(checkList2);
                logger.info("===========CheckThread 更新处理状态==========>>>" + checkList.getInvoiceNumber());
                executeService.execute(checkList);
                logger.info("===========CheckThread 启动查验脚本==========>>>" + checkList.getInvoiceNumber());
                logger.info("===========CheckThread end==========>>>" + checkList.getInvoiceNumber());
                logger.info("===========用时:" + (System.currentTimeMillis() - now.getTime()) / (1000 * 60) + "秒==========>>>" + checkList.getInvoiceNumber());
            }
        }catch (Exception e){

        }
    }
}
