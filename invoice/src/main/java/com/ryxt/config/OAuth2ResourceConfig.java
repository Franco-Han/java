package com.ryxt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceConfig extends ResourceServerConfigurerAdapter {
//public class OAuth2ResourceConfig {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
//                .antMatchers("/**").permitAll();
                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .antMatchers("/oss/**").permitAll()
                .antMatchers("/result/**").permitAll()
                .antMatchers("/app/**").permitAll()
                .antMatchers("/**").authenticated();

        http.headers().frameOptions().disable();
    }

}
