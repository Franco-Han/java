package com.ryxt.controller;

import com.ryxt.entity.User;
import com.ryxt.service.impl.UserServiceImpl;
import io.github.yedaxia.apidocs.ApiDoc;
import io.github.yedaxia.apidocs.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/ryxtOauth")
@Ignore
public class OauthController {

    @Autowired
    private UserServiceImpl sysUserService;
    /**
     * 取得当前用户
     *
     * @param user 用户信息
     * @return 用户信息
     */

    @RequestMapping(value = "/getCurrentUser")
    public Map<String,Object> user(Principal user) {
        Map<String,Object> map = new HashMap<String,Object>();
        User sysUser = sysUserService.getByUsername(user.getName());
        map.put("id",sysUser.getId());
        map.put("name",sysUser.getRealname());
        return map;
    }
}
