package com.hhdd.rpc.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author huanghedidi
 * @Date 2025/1/18 15:41
 */
public class SerialzerManager {

    private static Map<Integer, ISerializer> SERIALIZER_MAP = new HashMap<>();


    static {
        SERIALIZER_MAP.put(1, new JavaSerializer());
    }

    public static ISerializer getSerializer(int serialzeType) {
        return SERIALIZER_MAP.get(serialzeType);
    }

}
