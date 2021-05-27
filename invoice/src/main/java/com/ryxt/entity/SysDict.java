package com.ryxt.entity;

import lombok.Data;

import java.util.Date;

/**
 * 字典表
 */
@Data
public class SysDict {
    private String id;
    private String dictName;
    private String dictCode;
    private String description;
    private Integer delFlag;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private Integer type;

}
