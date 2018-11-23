package org.messtin.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;
import org.messtin.rpc.common.codec.RpcDecoder;
import org.messtin.rpc.common.codec.RpcEncoder;
import org.messtin.rpc.common.entity.RpcRequest;
import org.messtin.rpc.common.entity.RpcResponse;

/**
 * RpcClient uses to connect with server.
 *
 * @author majinliang
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {
    private static final Logger logger = Logger.getLogger(RpcClient.class);

    private String host;
    private int port;
    private RpcResponse response;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) {
        logger.info(String.format("Receive response: %s", msg));
        this.response = msg;
    }

    /**
     * Send request message to server.
     *
     * @param request
     * @return
     * @throws InterruptedException
     */
    public RpcResponse send(RpcRequest request) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            logger.info(String.format("Connecting with server {}:{}.", host, port));
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RpcEncoder(RpcRequest.class));
                            ch.pipeline().addLast(new RpcDecoder(RpcResponse.class));
                            ch.pipeline().addLast(RpcClient.this);
                        }
                    });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            ChannelFuture future = bootstrap.connect(host, port).sync();
            Channel channel = future.channel();
            channel.writeAndFlush(request).sync();
            logger.info(String.format("Sent request {} to server", request));
            channel.closeFuture().sync();
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }
}
