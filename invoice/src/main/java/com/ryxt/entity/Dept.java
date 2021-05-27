package com.ryxt.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_dept")
public class Dept  {
    @TableId
    private String id               ;
    private String parent_id        ;
    private String depart_name      ;
    private String depart_name_en   ;
    private String depart_name_abbr ;
    private String depart_order;
    private String description ;
    private String org_category;
    private String org_type    ;
    private String org_code    ;
    private String mobile      ;
    private String fax         ;
    private String address     ;
    private String memo        ;
    private String status      ;
    private String del_flag    ;
    private String create_by   ;
    private Date create_time ;
    private String update_by   ;
    private Date update_time ;

}
