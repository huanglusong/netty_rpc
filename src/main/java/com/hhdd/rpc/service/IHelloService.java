package com.hhdd.rpc.service;

import com.hhdd.rpc.annotation.RpcService;

/**
 * @Author HuangLusong
 * @Date 2025/1/18 20:55
 */
@RpcService("com.hhdd.rpc.service.HelloService")
public interface IHelloService {
    String sayHello(String name);
    static String getName(){
        return "";
    }
}
