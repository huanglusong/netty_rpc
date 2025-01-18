package com.hhdd.rpc.message;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author huanghedidi
 * @Date 2025/1/18 15:01
 */
@Slf4j
@Data
public class RpcResponseMessage extends RpcMessage {

    private Object returnValue;

    private Exception exception;




}
