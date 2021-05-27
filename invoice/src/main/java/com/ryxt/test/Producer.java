package com.ryxt.test;

import com.ryxt.base.redis.RedisService;
import com.ryxt.entity.CheckList;
import com.ryxt.util.SpringContextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    private static RedisService redisService  = (RedisService) SpringContextUtil.getBean("redisService");

    private static Basket basket = Basket.getInstance();
    private static BlockingQueue blockingQueue =  Basket.getBlockingQueue();
    @Override
    public void run() {
        try {
            while (true) {
                ArrayBlockingQueue<CheckList> list = (ArrayBlockingQueue<CheckList>)blockingQueue;

                List<CheckList> lists = new ArrayList<CheckList>();
                for(CheckList checkList : list){
                    lists.add(checkList);
                }
                CheckList checkList =  redisService.getCheckList(lists);
                if(checkList!=null){
                    basket.produce(checkList);
                }
            }
        } catch (InterruptedException ex) {
        }
    }
}
