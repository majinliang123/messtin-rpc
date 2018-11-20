package org.messtin.rpc.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.messtin.rpc.common.util.ProtostuffUtils;

import java.util.List;

/**
 * Parse byte stream from net to object.
 *
 * @author majinliang
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        int msgLen = byteBuf.readInt();
        if (byteBuf.readableBytes() < msgLen) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] msg = new byte[msgLen];
        byteBuf.readBytes(msg);
        Object obj = ProtostuffUtils.deserialize(msg, genericClass);
        list.add(obj);
    }
}
