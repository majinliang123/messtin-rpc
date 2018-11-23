package org.messtin.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;
import org.messtin.rpc.common.codec.RpcDecoder;
import org.messtin.rpc.common.codec.RpcEncoder;
import org.messtin.rpc.common.entity.RpcRequest;
import org.messtin.rpc.common.entity.RpcResponse;
import org.messtin.rpc.registry.ServiceRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The server we start.
 *
 * @author majinliang
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {
    private static final Logger logger = Logger.getLogger(RpcServer.class);

    private ServiceRegistry serviceRegistry;
    private String serviceAddress;

    private Map<String, Object> handlerMap = new ConcurrentHashMap<>();

    public RpcServer(ServiceRegistry serviceRegistry) {
        this(serviceRegistry, "localhost:8080");

    }

    public RpcServer(ServiceRegistry serviceRegistry, String serviceAddress) {
        this.serviceRegistry = serviceRegistry;
        this.serviceAddress = serviceAddress;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {

        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
        for (Object handler : serviceBeanMap.values()) {
            Class<?> clazz = handler.getClass();
            RpcService rpcServiceAnno = clazz.getAnnotation(RpcService.class);
            Class<?> type = rpcServiceAnno.value();
            String version = rpcServiceAnno.version();
            String serviceName = type.getName() + version;
            logger.info(String.format("Loaded handler class=%s >> serviceName=%s.", clazz.getName(), serviceName));
            handlerMap.put(serviceName, handler);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Starting server.");
        String[] address = serviceAddress.split(":");
        String host = address[0];
        int port = Integer.parseInt(address[1]);

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(host, port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RpcDecoder(RpcRequest.class));
                            ch.pipeline().addLast(new RpcEncoder(RpcResponse.class));
                            ch.pipeline().addLast(new RpcHandler(handlerMap));
                        }
                    });
            ChannelFuture future = serverBootstrap.bind().sync();
            for (String interfaceName : handlerMap.keySet()) {
                serviceRegistry.register(interfaceName, serviceAddress);
            }
            logger.info("Server start linsten at: " + future.channel().localAddress());
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
