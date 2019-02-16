package com.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author Wrecked
 * @date 2019.02.16  11:34
 */
public class ZooKeeperConnection {
    private ZooKeeper zoo;
    final CountDownLatch connectedSignal = new CountDownLatch(1);

    /**
     * Method to connect zookeeper ensemble.
     * param:
     *   connectionString - ZooKeeper集合主机
     *   sessionTimeout - 会话超时（以毫秒为单位）
     *   watcher - 实现“监视器”界面的对象。ZooKeeper集合通过监视器对象返回连接状态。
     */

    public ZooKeeper connect(String host) throws IOException, InterruptedException {
        zoo = new ZooKeeper(host, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent we) {

                if (we.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    connectedSignal.countDown();
                }
            }
        });
        connectedSignal.await();
        return zoo;
    }

    // Method to disconnect from zookeeper server
    public void close() throws InterruptedException {
        zoo.close();
    }

}
