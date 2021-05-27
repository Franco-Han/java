package com.ryxt.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("version_app")
public class AppVersion {
    private String id;

    @TableField(value = "version_code")
    private String versionCode;
    @TableField(value = "version_name")
    private String versionName;
    private String url;
    private String date;
    @TableField(value = "forced_update")
    private boolean forcedUpdate;
    @TableField(value = "is_release")
    private boolean isRelease;
    private int status;
    @TableField(value = "creator_id")
    private String creatorId;
    @TableField(value = "create_date")
    private Date createDate;
    private String content;
}
