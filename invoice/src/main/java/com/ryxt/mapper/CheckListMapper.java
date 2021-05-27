package com.ryxt.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.CheckListOcrVo;
import com.ryxt.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
* @Description: CheckListMapper.java
* @Author: uenpeng
* @Date: 2020/10/28
*/
public interface CheckListMapper extends BaseMapper<CheckList> {
    CheckList getFirstRecord(List<CheckList> list);


    Integer safeInsert(CheckList record);
}
