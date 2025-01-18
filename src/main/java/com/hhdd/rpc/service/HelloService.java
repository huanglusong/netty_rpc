package com.hhdd.rpc.service;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author huanghedidi
 * @Date 2025/1/18 16:18
 */
@Slf4j
public class HelloService {

    public String sayHello(String name) {
        log.info("hello {}!!!", name);
        return name;
    }
}
