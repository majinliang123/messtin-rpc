package org.messtin.rpc.sample;

import org.messtin.rpc.client.RpcProxy;
import org.messtin.rpc.registry.ServiceDiscovery;
import org.messtin.rpc.registry.zookeeper.ZookeeperServiceDiscovery;

public class Client {
    public static void main(String[] args) throws Exception {
        ServiceDiscovery serviceDiscovery = new ZookeeperServiceDiscovery("localhost:2181");
        RpcProxy proxy = new RpcProxy(serviceDiscovery);
        Hello hello = proxy.create(Hello.class);
        System.out.println(hello.say());
//        System.out.println(hello.talk());
    }
}
