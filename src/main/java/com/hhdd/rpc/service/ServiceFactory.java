package com.hhdd.rpc.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author huanghedidi
 * @Date 2025/1/18 16:14
 */
public class ServiceFactory {

    private static Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    public static Object getService(String intefaceName) {
        if (serviceMap.get(intefaceName) == null) {
            // 通过反射的方式实例化出对象
            try {
                Class<?> clazz = Class.forName(intefaceName);
                Object service = clazz.newInstance();
                serviceMap.putIfAbsent(intefaceName, service);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("获取服务失败！！");
            }
        }
        return serviceMap.get(intefaceName);
    }
}
