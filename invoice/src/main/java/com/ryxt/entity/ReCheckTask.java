package com.ryxt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 复查计划任务
 * @Author: uenpeng
 * @Date: 2020/10/15
 */
@Data
@TableName("re_check_task")
public class ReCheckTask {

    /**
     ID
     */
    private String id;
    /**
     用户id
     */
    @TableField(value = "user_id")
    private String userId;
    /**
     创建日期
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_date")
    private Date createDate;

    /**
     cron表达式
     */
    private String cron;

    /**
     任务名称
     */
    @TableField(value = "task_name")
    private String taskName;

    /**
     复查的数据
     */
    private String info;

    /**
     执行日期
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date cronDate;
}
