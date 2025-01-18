package com.hhdd.rpc;

import com.hhdd.rpc.handler.RpcMessageHandler;
import com.hhdd.rpc.protocol.ProcotolFrameDecoder;
import com.hhdd.rpc.protocol.RpcMessageCodec;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author huanghedidi
 * @Date 2025/1/18 16:30
 */
@Slf4j
public class RpcServer {

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        RpcMessageCodec rpcMessageCodec = new RpcMessageCodec();
        RpcMessageHandler rpcMessageHandler = new RpcMessageHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 用于切割报文的handler必须每次new出来
                    // 不可share
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    ch.pipeline().addLast(loggingHandler);
                    ch.pipeline().addLast(rpcMessageCodec);
                    ch.pipeline().addLast(rpcMessageHandler);
                }
            });
            Channel channel = serverBootstrap.bind(9898).sync().channel();
            log.debug("rpcServer启动成功！：{}", serverBootstrap);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
