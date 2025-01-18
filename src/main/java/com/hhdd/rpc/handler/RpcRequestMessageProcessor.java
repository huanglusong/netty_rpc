package com.hhdd.rpc.handler;

import com.hhdd.rpc.message.RpcMessage;
import com.hhdd.rpc.message.RpcRequestMessage;
import com.hhdd.rpc.message.RpcResponseMessage;
import com.hhdd.rpc.service.ServiceFactory;
import com.hhdd.rpc.util.ClassUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @Author huanghedidi
 * @Date 2025/1/18 16:02
 */
@Slf4j
public class RpcRequestMessageProcessor implements IRpcMessageProcessor {
    @Override
    public void doProcess(ChannelHandlerContext ctx, RpcMessage msg) {
        RpcResponseMessage rpcResponseMessage = new RpcResponseMessage();
        try {
            RpcRequestMessage rpcRequestMessage = (RpcRequestMessage) msg;
            rpcResponseMessage.setSequenceId(rpcRequestMessage.getSequenceId());
            String interfaceName = rpcRequestMessage.getInterfaceName();
            String methodName = rpcRequestMessage.getMethodName();
            Class[] paramTypes = rpcRequestMessage.getParamTypes();
            Object[] paramValue = rpcRequestMessage.getParamValue();
            Object service = ServiceFactory.getService(interfaceName);
            Class clzz = ClassUtil.forName(interfaceName);
            Method method = clzz.getDeclaredMethod(methodName, paramTypes);
            Object returnValue = method.invoke(service, paramValue);
            rpcResponseMessage.setReturnValue(returnValue);
        } catch (Exception e) {
            log.info("处理过程出错！！");
//            throw new RuntimeException("处理过程出错");
            String message = e.getCause().getMessage();
            Exception exception = new Exception(message);
            rpcResponseMessage.setException(e);
        }
        ctx.writeAndFlush(rpcResponseMessage);
    }
}
