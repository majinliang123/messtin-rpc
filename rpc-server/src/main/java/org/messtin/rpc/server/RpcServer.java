package org.messtin.rpc.server;

import org.messtin.rpc.registry.ServiceRegistry;

public class RpcServer {
    private ServiceRegistry serviceRegistry;

    public RpcServer(ServiceRegistry serviceRegistry){
        this.serviceRegistry = serviceRegistry;
    }
}
