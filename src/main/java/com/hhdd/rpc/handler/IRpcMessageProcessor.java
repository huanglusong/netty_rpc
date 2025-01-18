package com.hhdd.rpc.handler;

import com.hhdd.rpc.message.RpcMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author HuangLusong
 * @Date 2025/1/18 16:01
 */
public interface IRpcMessageProcessor {

    void doProcess(ChannelHandlerContext ctx, RpcMessage msg);
}
