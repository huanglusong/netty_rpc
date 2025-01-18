package com.hhdd.rpc.protocol;

/**
 * @Author huanghedidi
 * @Date 2025/1/18 15:29
 */
public interface ISerializer {

    // 反序列化方法
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    // 序列化方法
    <T> byte[] serialize(T object);


    /**
     * 枚举是最好的单例模式，序列化器只需一个很符合单理模式的场景
     */
    enum Serializer implements ISerializer {
        Java {
            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                return null;
            }

            @Override
            public <T> byte[] serialize(T object) {
                return new byte[0];
            }
        };


    }
}
