package com.ryxt.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
/**
* @Description: 系统信息
* @Author: uenpeng
* @Date: 2020/10/28
*/
@Data
@TableName("system_info")
public class SystemInfo {
    private String logo;
    private String systemName;
    private String favicon;
}
