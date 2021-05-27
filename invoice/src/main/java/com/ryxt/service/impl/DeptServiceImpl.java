package com.ryxt.service.impl;


import com.ryxt.entity.Dept;
import com.ryxt.mapper.DeptMapper;
import com.ryxt.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author uenpeng
 */
@Service
public class DeptServiceImpl implements DeptService {

	@Autowired
	public DeptMapper deptMapper;

	public Dept selectById(String id) {
		return deptMapper.selectById(id);
	}
}
