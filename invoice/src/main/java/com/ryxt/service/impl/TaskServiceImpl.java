package com.ryxt.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.CheckList;
import com.ryxt.mapper.CheckListMapper;
import com.ryxt.service.CheckService;
import com.ryxt.service.TaskService;
import com.ryxt.util.BaseAuthUtil;
import com.ryxt.util.CommonListResponse;
import com.ryxt.util.Const;
import com.ryxt.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	public CheckListMapper checkListMapper;
    /**
    * 获取待处理列表
    * @param
    * @return java.util.List<com.ryxt.entity.CheckList>
    * @throws Exception
    */
    @Override
    public List<CheckList> getTasks() {
        Page<CheckList> page = new Page<CheckList>(1,10);
        EntityWrapper<CheckList> wrapper = new EntityWrapper<CheckList>();
        wrapper.orderBy("apply_date",true);
        wrapper.ne("status","2");
        wrapper.eq("delete_flag",Const.NORMAL_STATUS);
        return checkListMapper.selectPage(page,wrapper);
    }
}
