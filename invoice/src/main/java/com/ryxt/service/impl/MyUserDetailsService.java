package com.ryxt.service.impl;


import com.alibaba.fastjson.JSON;
import com.ryxt.entity.User;
import com.ryxt.service.UserService;
import com.ryxt.util.CustomRequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

//    @Autowired
//    private PermissionService permissionService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = CustomRequestContext.getContext().getRequest();



        User sysUser = userService.getByUsername(username);

        if(sysUser == null){
            log.warn("用户{}不存在", username);
            throw new InternalAuthenticationServiceException("用户账号密码错误");
        }
        if(request!=null){
            String password = request.getParameter("password");
            if (!sysUser.getPassword().equals(passwordEncoder.encode(password))) {
                log.warn("用户{}密码不正确", username);
                throw new InternalAuthenticationServiceException("用户账号密码错误");
            }
        }
        if(!"1".equals(sysUser.getStatus())){
            log.warn("用户{}已被禁用,不允许登录系统", username);
            throw new InternalAuthenticationServiceException("用户账号已被禁用");
        }
//        List<SysPermission> permissionList = permissionService.findByUserId(sysUser.getId());
//        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
//        if (!CollectionUtils.isEmpty(permissionList)) {
//            for (SysPermission sysPermission : permissionList) {
//                authorityList.add(new SimpleGrantedAuthority(sysPermission.getCode()));
//            }
//        }

        log.info("登录成功！用户: {}", JSON.toJSONString(sysUser));
        return sysUser;
    }
}
