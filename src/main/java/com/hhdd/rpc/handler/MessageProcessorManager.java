package com.hhdd.rpc.handler;

import com.hhdd.rpc.message.RpcMessage;
import com.hhdd.rpc.message.RpcRequestMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author huanghedidi
 * @Date 2025/1/18 16:03
 */
public class MessageProcessorManager {

    private static Map<Class<? extends RpcMessage>, IRpcMessageProcessor> rpcMessageProcessorMap = new HashMap<>();


    static {
        rpcMessageProcessorMap.put(RpcRequestMessage.class, new RpcRequestMessageProcessor());
    }

    public static IRpcMessageProcessor getProcessor(Class<? extends RpcMessage> message) {
        return rpcMessageProcessorMap.get(message);
    }

}
