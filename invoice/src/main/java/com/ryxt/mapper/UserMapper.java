package com.ryxt.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ryxt.entity.User;
import org.apache.ibatis.annotations.Param;
/**
* @Description: UserMapper.java
* @Author: uenpeng
* @Date: 2020/10/28
*/
public interface UserMapper extends BaseMapper<User> {
    User getByUsername(@Param(value = "username") String username);
}
