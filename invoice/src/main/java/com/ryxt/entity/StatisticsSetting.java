package com.ryxt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
/**
* @Description: 统计表生效设置
* @Author: uenpeng
* @Date: 2020/10/28
*/
@Data
@TableName("statistics_setting")
public class StatisticsSetting {

    @TableField(value = "user_id")
    @TableId
    private String userId;
    /**
     * 简版表
     */
    private boolean simple;
    /**
     * 明细表
     */
    private boolean detail;
    /**
     * 信息对比统计表
     */
    private boolean compare;
    /**
     * 重复查询统计表
     */
    private boolean duplicate;
    /**
     * 复查结果变更统计表
     */
    private boolean change;
    /**
     * 购买方/销售方信息不符统计表
     */
    private boolean company;
    /**
     * 开票日期不符统计表
     */
    private boolean datematch;
    /**
     * 同一销售方统计表
     */
    private boolean oneseller;

}
