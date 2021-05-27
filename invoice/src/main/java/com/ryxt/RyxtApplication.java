package com.ryxt;


import com.ryxt.util.SpringContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableScheduling
@MapperScan({"com.ryxt.mapper","com.ryxt.common"})// 开始认证服务功能
@EnableAuthorizationServer
// 开启资源服务功能
@EnableResourceServer
@EnableAsync//启动异步
public class RyxtApplication implements ApplicationListener<ContextRefreshedEvent> {

    public static void main(String[] args) {
        ConfigurableApplicationContext run =  SpringApplication.run(RyxtApplication.class, args);
        run.registerShutdownHook();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public StartupRunner startupRunner(){
        return new StartupRunner();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        SpringContextUtil.setApplicationContext(contextRefreshedEvent.getApplicationContext());
    }

    @Bean(name = "taskExecutor")
    public Executor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }
}
