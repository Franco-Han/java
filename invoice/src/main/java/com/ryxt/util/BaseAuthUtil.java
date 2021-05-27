package com.ryxt.util;

import com.alibaba.fastjson.JSONObject;
import com.ryxt.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Map;

public class BaseAuthUtil {

    public static String getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return null;
        }

        if (authentication instanceof OAuth2Authentication) {
            User userAuthenticationDetails = (User)OAuth2Authentication.class.cast(authentication).getUserAuthentication().getPrincipal();

            return (String) userAuthenticationDetails.getId();
//            if(userAuthenticationDetails.containsKey("id")) {
//                Map userDetailed= (Map) userAuthenticationDetails.get("attributes");
//                return (String) userDetailed.get("userid");
//            }else {
//                Map principal = (Map) userAuthenticationDetails.get("principal");
//                return (String) principal.get("id");
//            }
        }

        return null;
    }
    public static User getCurrentUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return null;
        }
        User user = new User();
        if (authentication instanceof OAuth2Authentication) {
            user = (User)OAuth2Authentication.class.cast(authentication).getUserAuthentication().getPrincipal();
        }
        return user;
    }
    public static String getToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return null;
        }

        if (authentication instanceof OAuth2Authentication) {
            OAuth2AuthenticationDetails details =(OAuth2AuthenticationDetails) OAuth2Authentication.class.cast(authentication).getDetails();
            return details.getTokenValue();
        }

        return null;
    }
}
