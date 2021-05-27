package com.ryxt.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.BaseEntity;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.SysDict;
import com.ryxt.mapper.DictMapper;
import com.ryxt.service.DictService;
import com.ryxt.util.BeanTrans;
import com.ryxt.util.CommonListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author uenpeng
 */
@Service
public class DictServiceImpl implements DictService {

	@Autowired
	public DictMapper dictMapper;

	/**
	* 获取列表
	* @param sysDict
	* @return java.util.List<com.ryxt.entity.SysDict>
	* @throws Exception
	*/
	
	@Override
	public List<SysDict> getDictList(SysDict sysDict) {
		EntityWrapper<SysDict> wrapper = new EntityWrapper<SysDict>();
		return dictMapper.selectList(wrapper);
	}

	/**
	* 获取列表  分页
	* @param record
	* @return com.ryxt.util.CommonListResponse<com.ryxt.entity.SysDict>
	* @throws Exception
	*/
	
	@Override
	public CommonListResponse<SysDict> getListPage(BaseInput record) {
		CommonListResponse<SysDict> commonListResponse = new CommonListResponse<SysDict>(record);
		Page<SysDict> page = new Page<SysDict>(record.getPage(),record.getPageSize());
		EntityWrapper<SysDict> wrapper =  (EntityWrapper<SysDict>) BeanTrans.beanToNormal(record,SysDict.class);
		commonListResponse.setTotalCount(dictMapper.selectCount(wrapper));
		List<SysDict> list = dictMapper.selectPage(page,wrapper);
		commonListResponse.setList(list);
		return commonListResponse;
	}
}
