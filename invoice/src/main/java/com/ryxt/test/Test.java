package com.ryxt.test;

import com.ryxt.base.redis.RedisService;
import com.ryxt.entity.BaseEntity;
import com.ryxt.entity.CheckList;
import com.ryxt.util.SpringContextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    public static void main(String[] args) {
        ExecutorService service = Basket.getExecutorService();

        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        service.submit(producer);
        service.submit(consumer);
    }
}
