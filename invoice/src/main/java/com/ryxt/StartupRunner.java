package com.ryxt;

import com.ryxt.base.redis.RedisService;
import com.ryxt.base.redis.RedisUtil;
import com.ryxt.test.BlockingQueueTest;
import com.ryxt.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.List;

@Order(1)
public class StartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);
    @Override
    public void run(String... args) throws Exception {
//        BlockingQueueTest.testBasket();
    }
}
