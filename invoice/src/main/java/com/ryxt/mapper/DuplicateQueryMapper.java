package com.ryxt.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ryxt.entity.DuplicateQuery;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

public interface DuplicateQueryMapper extends BaseMapper<DuplicateQuery> {
    List<DuplicateQuery> getListPage(Map map);
    Integer getListCount(Map map);
}
