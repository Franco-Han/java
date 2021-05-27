package com.ryxt.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ryxt.entity.*;
import com.ryxt.exception.BusinessException;
import com.ryxt.mapper.UserMapper;
import com.ryxt.service.UserService;
import com.ryxt.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	public UserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
	@Override
	public User getByUsername(String username) {
		return sysUserMapper.getByUsername(username);
	}

	/**
	* 获取用户信息
	* @param 
	* @return com.ryxt.entity.User
	* @throws Exception
	*/
	
	@Override
	public User getUserAllInfo() {
		String userId = BaseAuthUtil.getCurrentUserId();
		return sysUserMapper.selectById(userId);
	}

    @Override
    public CommonListResponse<User> getListPage(BaseInput record) {
        CommonListResponse<User> commonListResponse = new CommonListResponse<User>(record);
        Page<User> page = new Page<User>(record.getPage(),record.getPageSize());
        EntityWrapper<User> wrapper =  (EntityWrapper<User>) BeanTrans.beanToNormal(record,User.class);
        wrapper.ne("del_flag","1");
        commonListResponse.setTotalCount(sysUserMapper.selectCount(wrapper));
        List<User> list = sysUserMapper.selectPage(page,wrapper);
        commonListResponse.setList(list);
        return commonListResponse;
    }

    @Override
    public User saveOrUpdate(User record) {

        String userId = BaseAuthUtil.getCurrentUserId();

        if(StringUtil.isNullEmpty(record.getId())){
            record.setId(StringUtil.generateUUID());
        }
        User record1 = sysUserMapper.selectById(record.getId());

        if(record1!=null){
            record.setUpdateBy(userId);
            record.setUpdateTime(new Date());
            sysUserMapper.updateById(record);
        }else {
            record.setCreateBy(userId);
            record.setCreateTime(new Date());
            record.setDelFlag(Const.NORMAL_STATUS);
            if(record.getPassword()==null){
                record.setPassword("123456");
            }
            record.setPassword(passwordEncoder.encode(record.getPassword()));
            sysUserMapper.insert(record);
        }
        return record;
    }

    @Override
    public int deleteById(String id) {
        User record = new User();
        record.setId(id);
        record.setDelFlag(Const.DELETE_STATUS);
        return sysUserMapper.updateById(record);
    }

    @Override
    public User selectById(String id) {
        return sysUserMapper.selectById(id);
    }

    @Override
    public int forbidden(String id,String status) {
        User record = new User();
        record.setId(id);
        record.setStatus("2".equals(status)?"1":"2");
        return sysUserMapper.updateById(record);
    }

    @Override
    public int changePassword(Password record) {
        User user = null;
        if(StringUtil.isNotNullEmpty(record.getUserId())){
          user =  sysUserMapper.selectById(record.getUserId());
        }else{
            // 当前用户
            String userId = BaseAuthUtil.getCurrentUserId();
            user =  sysUserMapper.selectById(userId);
        }
          if(user != null ){
              if(passwordEncoder.matches(record.getOldPassword(),user.getPassword())){
                  user.setPassword(passwordEncoder.encode(record.getNewPassword()));
                 return sysUserMapper.updateById(user);
              }else{
                  throw new BusinessException("密码错误");
              }
          }else{
              throw new BusinessException("账号不存在");
          }
    }

}
