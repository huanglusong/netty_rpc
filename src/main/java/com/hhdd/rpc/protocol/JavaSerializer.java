package com.hhdd.rpc.protocol;

import java.io.*;

/**
 * @Author huanghedidi
 * @Date 2025/1/18 15:39
 */
public class JavaSerializer implements ISerializer {
    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (T) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("反序列化失败");
        }
    }

    @Override
    public <T> byte[] serialize(T object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos);
            objectOutputStream.writeObject(object);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("序列化失败！！");
        }
    }
}
