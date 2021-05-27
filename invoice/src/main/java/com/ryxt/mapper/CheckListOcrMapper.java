package com.ryxt.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ryxt.entity.CheckList;
import com.ryxt.entity.CheckListOcrVo;

import java.util.List;
import java.util.Map;

public interface CheckListOcrMapper extends BaseMapper<CheckListOcrVo> {
     List<CheckListOcrVo> getCheckListOcrInfo(Map map);
     Integer getCheckListOcrCount(Map map);
}
