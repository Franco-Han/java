package com.ryxt.task.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 定时任务执行bean
 */
@Getter
@Setter
public class Task implements Serializable {
    private String taskId;
    private String corpId;
    private String signType;
    private String name;
    private String signTime;
    private String remind;
    private String remindTime;
    private String cron;
}
