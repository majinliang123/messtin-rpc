package org.messtin.rpc.registry.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Create zookeeper connect with server.
 *
 * @author majinliang
 */
public class ZooKeeperConnection {

    private CountDownLatch connectedSignal = new CountDownLatch(1);
    private ZooKeeper zooKeeper;

    public ZooKeeper connect(String address) throws IOException, InterruptedException {
        zooKeeper = new ZooKeeper(address, Constants.SESSION_TIMEOUT, event -> {
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                connectedSignal.countDown();
            }
        });
        connectedSignal.await();
        return zooKeeper;
    }

    public void close() throws InterruptedException {
        zooKeeper.close();
    }
}
