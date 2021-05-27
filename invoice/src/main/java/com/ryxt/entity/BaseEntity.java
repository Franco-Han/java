package com.ryxt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

@Data
public class BaseEntity {
    @TableField(exist = false)
    private int page = 1;
    @TableField(exist = false)
    private int pageSize = Integer.MAX_VALUE;
}
