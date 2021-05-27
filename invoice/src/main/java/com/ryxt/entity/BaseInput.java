package com.ryxt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class BaseInput {
    @TableField(exist = false)
    private int page = 1;
    @TableField(exist = false)
    private int pageSize = Integer.MAX_VALUE;

    private T data;

    private String orderBy = "";

    private boolean desc = true;
    private List<Map<String,Object>> options = new ArrayList<Map<String,Object>>();
}
