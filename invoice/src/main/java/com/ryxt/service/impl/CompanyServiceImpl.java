package com.ryxt.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.BaseInput;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.Company;
import com.ryxt.entity.User;
import com.ryxt.exception.BusinessException;
import com.ryxt.mapper.CompanyMapper;
import com.ryxt.mapper.UserMapper;
import com.ryxt.service.CompanyService;
import com.ryxt.service.UserService;
import com.ryxt.util.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	public CompanyMapper companyMapper;

    @Override
    public CommonListResponse<Company> getListPage(BaseInput record) {
        String userId = BaseAuthUtil.getCurrentUserId();
        CommonListResponse<Company> commonListResponse = new CommonListResponse<Company>(record);
        Page<Company> page = new Page<Company>(record.getPage(),record.getPageSize());
        EntityWrapper<Company> wrapper =  (EntityWrapper<Company>) BeanTrans.beanToNormal(record,Company.class);
        wrapper.where("user_id ="+"'"+userId+"'");
        wrapper.orderBy("create_date",false);
        commonListResponse.setTotalCount(companyMapper.selectCount(wrapper));
        List<Company> list = companyMapper.selectPage(page,wrapper);
        commonListResponse.setList(list);
        return commonListResponse;
    }


    @Override
    public Boolean saveOrUpdate(Company record) {

        String userId = BaseAuthUtil.getCurrentUserId();

        if(StringUtil.isNullEmpty(record.getId())){
            record.setId(StringUtil.generateUUID());
        }
        EntityWrapper<Company> wrapper = new EntityWrapper<>();
        String whereSql = "user_id='"+userId+"' and delete_flag <> '1'";

        if(record.getTin() != null && record.getTin() !=""){
            whereSql +="and (company_name ='"+record.getCompanyName()+"' or tin = '"+record.getTin()+"')";
        }else{
            whereSql +="and company_name ='"+record.getCompanyName()+"'";
        }
        Company record1 = companyMapper.selectById(record.getId());
        if(record1!=null) {
            // 更新
            whereSql+=" and id <>'"+record.getId()+"'";
        }else{
            // 新建
        }

        wrapper.where(whereSql);
        List<Company> list =companyMapper.selectList(wrapper);
        if(list!=null&& list.size()>0){
            return false;
        }
        if(record1!=null) {
            // 更新
            record.setDeleteFlag(Const.NOENABLE_STATUS);
            companyMapper.updateById(record);
        }else {
            // 新建
            record.setUserId(userId);
            record.setCreateDate(new Date());
            record.setDeleteFlag(Const.NOENABLE_STATUS);
            companyMapper.insert(record);
        }
        return true;
    }

    @Override
    public int deleteById(String id) {
        Company record = new Company();
        record.setId(id);
        record.setDeleteFlag(Const.DELETE_STATUS);
        return companyMapper.updateById(record);
    }
    @Override
    @Transactional
    public int delete(List<Company> companies) {
       int count = 0;
       for(Company company : companies){
           count += deleteById(company.getId());
       }
        return count;
    }

    @Override
    public Company selectById(String id) {
        return companyMapper.selectById(id);
    }

}
