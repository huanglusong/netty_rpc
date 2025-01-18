package com.hhdd.rpc.message;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author huanghedidi
 * 协议定义
 * 1 魔数 2 版本 3 序列化方式 4 内容
 * @Date 2025/1/18 11:30
 */
@Slf4j
@Data
public class RpcMessage implements Serializable {

    private byte version = (byte) 1;

    private int sequenceId;

    private int messageType;

    /**
     * 请求类型 byte 值
     */
    public static final int RPC_MESSAGE_TYPE_REQUEST = 101;
    /**
     * 响应类型 byte 值
     */
    public static final int RPC_MESSAGE_TYPE_RESPONSE = 102;

    private static Map<Integer, Class<? extends RpcMessage>> messageMap = new HashMap<>();

    static {
        messageMap.put(RPC_MESSAGE_TYPE_REQUEST, RpcRequestMessage.class);
        messageMap.put(RPC_MESSAGE_TYPE_RESPONSE, RpcResponseMessage.class);
    }

    public static Class<? extends RpcMessage> getRpcMessageClass(int rpcMessageType) {
        return messageMap.get(rpcMessageType);
    }

}
