package com.ryxt.test;

import com.ryxt.base.redis.RedisService;
import com.ryxt.base.redis.RedisUtil;
import com.ryxt.entity.CheckList;
import com.ryxt.mapper.UserMapper;
import com.ryxt.util.SpringContextUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockingQueueTest {
    private static RedisService redisService  = (RedisService) SpringContextUtil.getBean("redisService");

    /** *//**
     * 定义装苹果的篮子
     */
    public static class Basket{
        // 篮子，能够容纳3个苹果
// 篮子，能够容纳3个苹果
        BlockingQueue basket = new ArrayBlockingQueue< CheckList>(3);
        // 生产苹果，放入篮子
        public void produce(CheckList checkList) throws InterruptedException{
            // put方法放入一个苹果，若basket满了，等到basket有位置
            redisService.start(checkList);
            basket.put(checkList);
            System.out.println("添加一个任务"+checkList.getUrl()+",当前任务数量：" + basket.size());
        }
        // 消费苹果，从篮子中取走
        public void consume() throws InterruptedException{
            // get方法取出一个苹果，若basket为空，等到basket有苹果为止
            Thread.sleep(1500);
            CheckList checkList = (CheckList) basket.take();
            redisService.finish(checkList);
            System.out.println("完成任务"+checkList.getUrl()+"移除,当前任务数量：" + basket.size());
        }
    }
    //　测试方法
    public static void testBasket() {
        // 建立一个装苹果的篮子
        final Basket basket = new Basket();
        // 定义苹果生产者
        class Producer implements Runnable {
            public void run() {
                try {
                    while (true) {
                        ArrayBlockingQueue<CheckList> list = (ArrayBlockingQueue<CheckList>)basket.basket;

                        List<CheckList> lists = new ArrayList<CheckList>();
                        for(CheckList checkList : list){
                            lists.add(checkList);
                        }
                       CheckList checkList =  redisService.getCheckList(lists);
//                        if(basket.basket.size()<3) {
                        if(checkList!=null){
                            basket.produce(checkList);
                        }
                            Thread.sleep(1000);
//                        }
                    }
                } catch (InterruptedException ex) {
                }
            }
        }
        // 定义苹果消费者
        class Consumer implements Runnable {
            public void run() {
                try {
                    while (true) {
//                        if(basket.basket.size()>0){
                            // 消费苹果
                            basket.consume();
//                        }
                    }
                } catch (InterruptedException ex) {
                }
            }
        }

        ExecutorService service = Executors.newCachedThreadPool();
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        service.submit(producer);
        service.submit(consumer);

//        Consumer consumer2 = new Consumer();
//
//        service.submit(consumer2);
//        service.shutdownNow();
    }

    public static void main(String[] args) {
        BlockingQueueTest.testBasket();
    }
}
