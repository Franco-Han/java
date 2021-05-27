package com.ryxt.test;

import com.ryxt.base.redis.RedisService;
import com.ryxt.util.SpringContextUtil;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private static RedisService redisService  = (RedisService) SpringContextUtil.getBean("redisService");

    private static Basket basket = Basket.getInstance();
    private static BlockingQueue blockingQueue =  Basket.getBlockingQueue();

    public void run() {
        try {
            while (true) {
                basket.consume();
            }
        } catch (InterruptedException ex) {
        }
    }
}
