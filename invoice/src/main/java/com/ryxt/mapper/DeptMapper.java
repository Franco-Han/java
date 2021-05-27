package com.ryxt.mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ryxt.entity.Dept;

import java.util.List;
public interface DeptMapper extends BaseMapper<Dept> {
    List<com.ryxt.entity.Dept> selectDepts(com.ryxt.entity.Dept dept);
}
