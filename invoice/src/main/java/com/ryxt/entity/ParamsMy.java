package com.ryxt.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName(value = "params_my")
public class ParamsMy {

    @TableId
    private String id          ;
    @TableField(value = "user_id")
    private String userId    ;
    @TableField(value = "key")
    private String key    ;
    @TableField(value = "value")
    private String value    ;
}
