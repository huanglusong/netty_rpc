package com.hhdd.rpc.handler;

import com.hhdd.rpc.message.RpcMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author huanghedidi
 * @Date 2025/1/18 16:00
 */
@ChannelHandler.Sharable
public class RpcMessageHandler extends SimpleChannelInboundHandler<RpcMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {
        Class<? extends RpcMessage> clazz = msg.getClass();
        IRpcMessageProcessor processor = MessageProcessorManager.getProcessor(clazz);
        processor.doProcess(ctx, msg);
    }
}
