package org.messtin.rpc.registry.zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.messtin.rpc.registry.ServiceDiscovery;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Discover service by zookeeper.
 *
 * @author majinliang
 */
public class ZookeeperServiceDiscovery implements ServiceDiscovery {
    private static final Logger logger = Logger.getLogger(ZookeeperServiceDiscovery.class);
    private ZooKeeper zooKeeper;

    public ZookeeperServiceDiscovery(String zkAddress) throws IOException, InterruptedException {
        ZooKeeperConnection connection = new ZooKeeperConnection();
        zooKeeper = connection.connect(zkAddress);
        logger.info(String.format("Connect to %s successfully.", zkAddress));
    }

    @Override
    public String discover(String name) throws KeeperException, InterruptedException {
        String servicePath = Constants.ZK_REGISTRY_PATH + Constants.VIRGULE + name;
        Stat serviceStat = zooKeeper.exists(servicePath, true);
        if (serviceStat == null) {
            throw new RuntimeException(String.format("Can not find any service node on path: %s", servicePath));
        }
        logger.info(String.format("Node %s existed and its version is %s", servicePath, serviceStat.getVersion()));
        List<String> nodes = zooKeeper.getChildren(servicePath, true);
        if (nodes.isEmpty()) {
            throw new RuntimeException(String.format("Can not find any address node on path: %s", servicePath));
        }
        String nodeName = null;
        if (nodes.size() == 1) {
            nodeName = nodes.get(0);
        } else {
            nodeName = nodes.get(ThreadLocalRandom.current().nextInt(nodes.size()));
        }
        String address = new String(zooKeeper.getData(servicePath + Constants.VIRGULE + nodeName, true, new Stat()));
        logger.info(String.format("Get address %s", address));
        return address;
    }
}
