package org.messtin.rpc.registry.zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.messtin.rpc.registry.ServiceRegistry;

import java.io.IOException;

/**
 * Register server at zookeeper.
 *
 * @author majinliang
 */
public class ZookeeperServiceRegistry implements ServiceRegistry {
    private static final Logger logger = Logger.getLogger(ZookeeperServiceRegistry.class);
    private ZooKeeper zooKeeper;

    public ZookeeperServiceRegistry(String zkAddress) throws IOException, InterruptedException {
        ZooKeeperConnection connection = new ZooKeeperConnection();
        zooKeeper = connection.connect(zkAddress);
        logger.info(String.format("Connect to %s successfully.", zkAddress));
    }

    @Override
    public void register(String name, String address) throws KeeperException, InterruptedException {
        Stat registryStat = zooKeeper.exists(Constants.ZK_REGISTRY_PATH, true);
        if (registryStat == null) {
            zooKeeper.create(Constants.ZK_REGISTRY_PATH, null,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            logger.info(String.format("Create registry node %s.", Constants.ZK_REGISTRY_PATH));
        }

        String servicePath = Constants.ZK_REGISTRY_PATH + Constants.VIRGULE + name;
        Stat serviceStat = zooKeeper.exists(servicePath, true);
        if (serviceStat == null) {
            zooKeeper.create(servicePath, null,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            logger.info(String.format("Create service node %s.", servicePath));
        }

        String addressPath = servicePath + Constants.VIRGULE + "/address-";
        zooKeeper.create(addressPath, address.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        logger.info(String.format("Create address node %s.", addressPath));
    }
}
