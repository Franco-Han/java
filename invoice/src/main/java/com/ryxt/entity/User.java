package com.ryxt.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@Data
@TableName(value = "sys_user")
public class User implements UserDetails {

    @TableId
    private String id          ;
    private String username    ;
    private String realname    ;
    private String password    ;
    private String salt        ;
    private String avatar      ;
    private String birthday    ;
    private String sex         ;
    private String email       ;
    private String phone       ;
    private String orgCode    ;
    private String status      ;
    private String delFlag    ;
    private String work_no     ;
    private String post        ;
    private String telephone   ;
    private String createBy   ;
    private Date createTime ;
    private String updateBy   ;
    private Date updateTime ;
    private String identity	;
    private String departIds	;
    private String role;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
