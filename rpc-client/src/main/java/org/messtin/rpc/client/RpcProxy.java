package org.messtin.rpc.client;

import org.messtin.rpc.common.entity.RpcRequest;
import org.messtin.rpc.common.entity.RpcResponse;
import org.messtin.rpc.registry.ServiceDiscovery;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class RpcProxy {
    private ServiceDiscovery serviceDiscovery;

    public RpcProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T create(Class<T> clazz){
        return create(clazz, "");
    }

    public <T> T create(Class<T> clazz, String serviceVersion) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                (Object proxy, Method method, Object[] args) -> {
                    RpcRequest request = new RpcRequest();
                    String requestId= UUID.randomUUID().toString();
                    String interfaceName = clazz.getName();
                    String methodName = method.getName();
                    Class<?>[] paramTypes = method.getParameterTypes();
                    request.setRequestId(requestId);
                    request.setInterfaceName(interfaceName);
                    request.setServiceVersion(serviceVersion);
                    request.setMethodName(methodName);
                    request.setParamTypes(paramTypes);
                    request.setParams(args);

                    String serviceName = interfaceName+serviceVersion;
                    String[] address = serviceDiscovery.discover(serviceName).split(":");
                    System.out.println(serviceDiscovery.discover(serviceName));
                    String host = address[0];
                    int port = Integer.parseInt(address[1]);
                    RpcClient client = new RpcClient(host, port);
                    RpcResponse response = client.send(request);
                    if (response.getException() != null){
                        throw response.getException();
                    } else {
                        return response.getResult();
                    }
                }
        );
    }
}
