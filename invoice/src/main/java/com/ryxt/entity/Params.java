package com.ryxt.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName(value = "params")
public class Params {

    @TableId
    private String id          ;
    @TableField(value = "user_id")
    private String userId    ;
    private String key    ;
    private String value        ;
    @TableField(value = "delete_flag")
    private String deleteFlag       ;
}
