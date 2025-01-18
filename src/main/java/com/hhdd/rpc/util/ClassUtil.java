package com.hhdd.rpc.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author huanghedidi
 * @Date 2025/1/18 16:22
 */
public class ClassUtil {

    private static Map<String, Class> map = new ConcurrentHashMap<>();

    public static Class forName(String className) {
        if (!map.containsKey(className)) {
            try {
                Class<?> clazz = Class.forName(className);
                map.putIfAbsent(className, clazz);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("获取class对象失败！！！");
            }
        }
        return map.get(className);
    }
}
