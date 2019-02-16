package com.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author Wrecked
 * @date 2019.02.16  11:38
 */
public class ZKSetData {
    private static ZooKeeper zk;
    private static ZooKeeperConnection conn;

    // Method to update the data in a znode. Similar to getData but without watcher.
    public static void update(String path, byte[] data) throws
            KeeperException, InterruptedException {
         // path- Znode路径
        //  data - 要存储在指定znode路径中的数据。
       // version- znode的当前版本。每当数据更改时，ZooKeeper会更新znode的版本号。
        zk.setData(path, data, zk.exists(path, true).getVersion());
    }

    public static void main(String[] args) throws InterruptedException, KeeperException {
        String path = "/MyFirstZnode";
        byte[] data = "Success".getBytes(); //Assign data which is to be updated.

        try {
            conn = new ZooKeeperConnection();
            zk = conn.connect("localhost");
            update(path, data); // Update znode data to the specified path
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
