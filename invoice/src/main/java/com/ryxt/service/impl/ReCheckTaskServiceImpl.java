package com.ryxt.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.ReCheckTask;
import com.ryxt.mapper.CheckListMapper;
import com.ryxt.mapper.ReCheckTaskMapper;
import com.ryxt.quartz.QuartzManager;
import com.ryxt.quartz.ReCheckTaskJob;
import com.ryxt.service.ReCheckTaskService;
import com.ryxt.util.BaseAuthUtil;
import com.ryxt.util.Const;
import com.ryxt.util.DateUtils;
import com.ryxt.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ReCheckTaskServiceImpl implements ReCheckTaskService {

	@Autowired
	public ReCheckTaskMapper reCheckTaskMapper;
    @Autowired
    public CheckListMapper checkListMapper;
    @Autowired
    QuartzManager quartzManager;

    /**
    * 保存任务
    * @param record
    * @return com.ryxt.entity.ReCheckTask
    * @throws Exception
    */
    @Override
    public ReCheckTask saveOrUpdate(ReCheckTask record) {

        String userId = BaseAuthUtil.getCurrentUserId();
        record.setId(StringUtil.generateUUID());
        record.setUserId(userId);
        record.setCreateDate(new Date());
        record.setCron(DateUtils.getCron(record.getCronDate()));
        record.setTaskName("re_check_task_"+System.currentTimeMillis());
        reCheckTaskMapper.insert(record);
        quartzManager.start(record);
        return record;
    }

    /**
    * 获取任务
    * @param id
    * @return com.ryxt.entity.ReCheckTask
    * @throws Exception
    */
    
    @Override
    public ReCheckTask selectById(String id) {
        return reCheckTaskMapper.selectById(id);
    }

    /**
    * 开始执行任务
    * @param t
    * @return void
    * @throws Exception
    */
    
    @Override
    public ReCheckTask start(ReCheckTask t) {
        if(t!=null){
            List<CheckList> list = (List<CheckList>)JSONArray.parseArray(t.getInfo(), CheckList.class);

            if(list!=null){
                for(CheckList checkList:list){
                    // 今天的单据 不重新生成查验单
                    if(DateUtils.isToday(checkList.getCreateDate())){
                        checkList.setStatus("0");
                        checkList.setDeleteFlag(Const.NORMAL_STATUS);
                        checkList.setUpdateDate(new Date());
                        checkList.setApplyDate(new Date());
                        checkList.setReason("");
                        checkList.setReCheck(true);
                        checkList.setReCheckId(checkList.getId());
                        checkList.setReCheckResult(checkList.getResult());
                        checkList.setResult("");
                        checkListMapper.updateById(checkList);
                    }else{
                        checkList.setReCheckId(checkList.getId());
                        checkList.setStatus("0");
                        checkList.setDeleteFlag(Const.NORMAL_STATUS);
                        checkList.setCreateDate(new Date());
                        checkList.setApplyDate(new Date());
                        checkList.setUpdateDate(new Date());
                        checkList.setReason("");
                        checkList.setReCheck(true);
                        checkList.setId(StringUtil.generateUUID());
                        checkList.setReCheckResult(checkList.getResult());
                        checkList.setResult("");
                        checkListMapper.insert(checkList);
                    }
                }
            }
        }

        return  null;
    }
}
