package com.enjoy.zookeeper.javaapi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class TestCreateSession {
    //zookeeper服务地址
    private static final String SERVER = "192.168.64.132,192.168.64.135,192.168.64.136";

    //会话超时时间
    private final int SESSION_TIMEOUT = 30000;

    @Test
    public void testSession1() throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper(SERVER,SESSION_TIMEOUT,null);
        System.out.println(zooKeeper);
        System.out.println(zooKeeper.getState());
    }

    //发令枪
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Test
    public void testSession2() throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper(SERVER, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if ( watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    //确认已经连接完毕后在进行操作
                    countDownLatch.countDown();
                    System.out.println("已经获得了连接");
                }
            }
        });

        //连接完成之前先等待
        countDownLatch.await();
        System.out.println(zooKeeper.getState());
    }

}
