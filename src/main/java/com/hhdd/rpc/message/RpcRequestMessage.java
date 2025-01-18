package com.hhdd.rpc.message;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author huanghedidi
 * @Date 2025/1/18 15:01
 */
@Slf4j
@Data
public class RpcRequestMessage extends RpcMessage {

    private String interfaceName;

    private String methodName;

    private Class returnType;

    // 方法参数
    private Class[] paramTypes;
    // 方法值
    private Object[] paramValue;

}
