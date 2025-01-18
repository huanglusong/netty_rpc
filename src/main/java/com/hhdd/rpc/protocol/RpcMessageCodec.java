package com.hhdd.rpc.protocol;

import com.hhdd.rpc.message.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.hhdd.rpc.message.RpcMessage.RPC_MESSAGE_TYPE_REQUEST;

/**
 * rpc消息的编、解码器
 * <p>
 * rpc消息定义
 * 1-4：魔数 5：版本号 6：序列化算法 7：消息类型 8-11：sequenceId 12:填充对齐
 * 13-16：长度位
 *
 * @Author huanghedidi
 * @Date 2025/1/18 15:17
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcMessageCodec extends MessageToMessageCodec<ByteBuf, RpcMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        // 魔数
        byteBuf.writeBytes("hhdd".getBytes());
        // 版本号
        byteBuf.writeByte(1);
        // 序列化类型
        byteBuf.writeByte(1);
        // 消息类型
        byteBuf.writeByte(RPC_MESSAGE_TYPE_REQUEST);
        // sequenceId
        byteBuf.writeInt(msg.getSequenceId());
        // 填充
        byteBuf.writeByte(0);
        byte[] body = SerialzerManager.getSerializer(1).serialize(msg);
        // 长度
        byteBuf.writeInt(body.length);
        byteBuf.writeBytes(body);
        // 内容
        out.add(byteBuf);

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int magicNum = msg.readInt();
        byte version = msg.readByte();
        // 序列化方式
        byte serilizeType = msg.readByte();
        byte messageType = msg.readByte();
        int sequenceId = msg.readInt();
        // 填充位
        msg.readByte();
        // 长度位
        int length = msg.readInt();
        // 读取内容
        byte[] body = new byte[length];
        msg.readBytes(body);
        // 通过序列化方式将body转化为对应的java类型的message对象
        // 序列化算法1代表使用java序列化器
        ISerializer serializer = SerialzerManager.getSerializer(serilizeType);
        Class<? extends RpcMessage> rpcMessageClass = RpcMessage.getRpcMessageClass(messageType);
        RpcMessage rpcMessage = serializer.deserialize(rpcMessageClass, body);
        out.add(rpcMessage);
    }
}
