package com.ryxt.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ryxt.entity.CommonSeller;

import java.util.List;
import java.util.Map;

public interface CommonSellerMapper extends BaseMapper<CommonSeller>{
    Integer findCount(Map map);
    List<CommonSeller> getListPage(Map map);
}
