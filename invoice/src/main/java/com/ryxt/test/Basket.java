package com.ryxt.test;

import com.ryxt.entity.CheckList;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Basket {

    private static Basket basket = new Basket();
    private static BlockingQueue blockingQueue = new ArrayBlockingQueue<CheckList>(3);
    private static   ExecutorService service = Executors.newCachedThreadPool();

    private Basket(){}
    public static Basket getInstance(){
        return basket;
    }
    public static BlockingQueue getBlockingQueue(){
        return blockingQueue;
    }
    public static ExecutorService getExecutorService(){
        return service;
    }
    public void produce(CheckList checkList) throws InterruptedException{
        blockingQueue.put(checkList);
        System.out.println("添加一个任务"+checkList.getUrl()+",当前任务数量：" + blockingQueue.size());
    }
    public void consume() throws InterruptedException{
        CheckList checkList = (CheckList) blockingQueue.take();
        System.out.println("完成任务"+checkList.getUrl()+"移除,当前任务数量：" + blockingQueue.size());
    }

}
