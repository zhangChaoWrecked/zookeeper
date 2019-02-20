package com.apacheCurator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

/**
 * @author ZhangChao
 * @date 2019.02.18  16:09
 */
public class CuratorClient {
    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        client.start();
        client.create().forPath("/head", new byte[0]);
        client.delete().inBackground().forPath("/head");
        client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/head/child", new byte[0]);
        client.getData().watched().inBackground().forPath("/test");
//        Distributed Lock
        InterProcessMutex lock = new InterProcessMutex(client, "/my-lock");
        if (lock.acquire(1000, TimeUnit.MICROSECONDS)) {
            try {
                // do some work inside of the critical section here
            } finally {
                lock.release();
            }
        }
    }

}
