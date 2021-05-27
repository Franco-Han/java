package com.ryxt.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ryxt.entity.SystemInfo;
import com.ryxt.entity.User;
import com.ryxt.mapper.SystemMapper;
import com.ryxt.service.SystemService;
import com.ryxt.util.BaseAuthUtil;
import com.ryxt.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemServiceImpl implements SystemService {

	@Autowired
	public SystemMapper systemMapper;

    @Override
    public SystemInfo saveOrUpdate(SystemInfo record) {
        EntityWrapper<SystemInfo> wrapper = new EntityWrapper<SystemInfo>();
        systemMapper.update(record,wrapper);
        return record;
    }

    @Override
    public SystemInfo select() {
        return systemMapper.selectOne(new SystemInfo());
    }

}
