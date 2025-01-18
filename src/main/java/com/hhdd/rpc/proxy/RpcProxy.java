package com.hhdd.rpc.proxy;

import com.hhdd.rpc.annotation.RpcService;
import com.hhdd.rpc.message.RpcRequestMessage;
import com.hhdd.rpc.protocol.ProcotolFrameDecoder;
import com.hhdd.rpc.protocol.RpcMessageCodec;
import com.sun.istack.internal.Nullable;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static com.hhdd.rpc.message.RpcMessage.RPC_MESSAGE_TYPE_REQUEST;

/**
 * rpc发起代理
 * 用来隐藏客户端接出的细节
 * 让使用起来更加轻松
 *
 * @Author huanghedidi
 * @Date 2025/1/18 20:48
 */
@Slf4j
public class RpcProxy implements InvocationHandler {
    private Class interfaceClass;
    private String interfaceName;
    public RpcProxy(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
//        RpcService annotation = (RpcService) this.interfaceClass.getDeclaredAnnotation(RpcService.class);
        RpcService rpcService = findAnnotation(this.interfaceClass, RpcService.class);
        this.interfaceName = rpcService.value();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        send(methodName, parameterTypes, args);
        return null;
    }

    private  <A extends Annotation> A findAnnotation(Class<?> clazz, @Nullable Class<A> annotationType) {
        A annotation = clazz.getDeclaredAnnotation(annotationType);
        return annotation;
    }


    public void send(String methodName, Class[] parameterTypes, Object[] args) {
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        RpcMessageCodec rpcMessageCodec = new RpcMessageCodec();
        Bootstrap boostrap = new Bootstrap().group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
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
        rpcRequestMessage.setInterfaceName(interfaceName);
        rpcRequestMessage.setMethodName(methodName);
        rpcRequestMessage.setParamTypes(parameterTypes);
        rpcRequestMessage.setParamValue(args);
        channel.writeAndFlush(rpcRequestMessage);
    }

}
