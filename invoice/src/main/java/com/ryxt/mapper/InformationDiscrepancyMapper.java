package com.ryxt.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ryxt.entity.InformationDiscrepancy;
import com.ryxt.entity.InvoiceDateDiscrepancy;

import java.util.List;
import java.util.Map;

public interface InformationDiscrepancyMapper extends BaseMapper<InformationDiscrepancy> {
    List<InformationDiscrepancy> getListPage(Map map);
    Integer getListCount(Map map);
}
