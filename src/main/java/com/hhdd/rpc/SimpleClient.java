package com.hhdd.rpc;

import com.hhdd.rpc.handler.RpcMessageHandler;
import com.hhdd.rpc.message.RpcRequestMessage;
import com.hhdd.rpc.protocol.ProcotolFrameDecoder;
import com.hhdd.rpc.protocol.RpcMessageCodec;
import com.hhdd.rpc.protocol.SerialzerManager;
import com.hhdd.rpc.service.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.hhdd.rpc.message.RpcMessage.RPC_MESSAGE_TYPE_REQUEST;

/**
 * @Author huanghedidi
 * @Date 2025/1/18 16:41
 */
@Slf4j
public class SimpleClient {

    public static void main(String[] args) throws IOException {

        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        RpcMessageCodec rpcMessageCodec = new RpcMessageCodec();
        RpcMessageHandler rpcMessageHandler = new RpcMessageHandler();
        Bootstrap boostrap = new Bootstrap().group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new ProcotolFrameDecoder());
                        ch.pipeline().addLast(loggingHandler);
                        ch.pipeline().addLast(rpcMessageCodec);
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("收到的响应消息是：{}", msg);
                                super.channelRead(ctx, msg);
                            }
                        });
                    }
                });
        ChannelFuture cf = boostrap.connect("localhost", 9898);
        cf.syncUninterruptibly();
        Channel channel = cf.channel();
        RpcRequestMessage rpcRequestMessage = new RpcRequestMessage();
        rpcRequestMessage.setSequenceId(9898);
        rpcRequestMessage.setMessageType(RPC_MESSAGE_TYPE_REQUEST);
        rpcRequestMessage.setInterfaceName(HelloService.class.getName());
        rpcRequestMessage.setMethodName("sayHello");
        rpcRequestMessage.setParamTypes(new Class[]{String.class});
        rpcRequestMessage.setParamValue(new Object[]{"hhdd"});
        channel.writeAndFlush(rpcRequestMessage);
        System.in.read();
    }

    public static byte[] buildReq() {
        RpcRequestMessage rpcRequestMessage = new RpcRequestMessage();
        rpcRequestMessage.setSequenceId(9898);
        rpcRequestMessage.setMessageType(RPC_MESSAGE_TYPE_REQUEST);
        rpcRequestMessage.setInterfaceName(HelloService.class.getName());
        rpcRequestMessage.setMethodName("sayHello");
        rpcRequestMessage.setParamTypes(new Class[]{String.class});
        rpcRequestMessage.setParamValue(new Object[]{"hhdd"});

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        // 魔数
        byteBuf.writeBytes("hhdd".getBytes());
        // 版本号
        byteBuf.writeByte(1);
        // 序列化类型
        byteBuf.writeByte(1);
        // 消息类型
        byteBuf.writeByte(RPC_MESSAGE_TYPE_REQUEST);
        // sequenceId
        byteBuf.writeInt(rpcRequestMessage.getSequenceId());
        // 填充
        byteBuf.writeByte(0);
        byte[] body = SerialzerManager.getSerializer(1).serialize(rpcRequestMessage);
        // 长度
        byteBuf.writeInt(body.length);
        // 内容
        byteBuf.writeBytes(body);
        return ByteBufUtil.getBytes(byteBuf);
    }

}
