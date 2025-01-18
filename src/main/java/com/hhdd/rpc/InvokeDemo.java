package com.hhdd.rpc;

import com.hhdd.rpc.proxy.RpcProxy;
import com.hhdd.rpc.service.IHelloService;

import java.lang.reflect.Proxy;

/**
 * @Author huanghedidi
 * @Date 2025/1/18 20:56
 */
public class InvokeDemo {
    public static void main(String[] args) {
        RpcProxy rpcProxy = new RpcProxy(IHelloService.class);
        IHelloService helloService = (IHelloService) Proxy.newProxyInstance(IHelloService.class.getClassLoader(), new Class[]{IHelloService.class}, rpcProxy);
        helloService.sayHello("hhdd");
    }
}
