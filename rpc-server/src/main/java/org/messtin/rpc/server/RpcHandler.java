package org.messtin.rpc.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;
import org.messtin.rpc.common.entity.RpcRequest;
import org.messtin.rpc.common.entity.RpcResponse;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Use to handle request from client.
 *
 * @author majinliang
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {
    Logger logger = Logger.getLogger(RpcHandler.class);

    private Map<String, Object> handlerMap;

    public RpcHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        logger.info(String.format("Receive request %s", request));
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object result = handle(request);
            response.setResult(result);
        } catch (Exception ex) {
            response.setException(ex);
        }
        logger.info(String.format("Send response {}.", response));
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handle(RpcRequest request) throws Exception {
        logger.info("Handling request.");
        String serviceName = request.getInterfaceName() + request.getServiceVersion();
        String methodName = request.getMethodName();
        Class<?>[] paramTypes = request.getParamTypes();
        Object[] params = request.getParams();
        Object handler = handlerMap.get(serviceName);
        Method method = handler.getClass().getMethod(methodName, paramTypes);
        return method.invoke(handler, params);
    }
}
