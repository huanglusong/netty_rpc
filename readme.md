# 对rpc的理解
远程调用
最朴素意义上指的其实就是客户端和服务端通信
任何接口调用我觉得都可以视作rpc
百度点击搜索何尝不是一次rpc呢

# 设计rpc
## 接入和接出
既然是客户端和服务端通信，必然要有接入接出
可以使用netty框架，其基于nio做了二次封装

## rpc协议要素
### rpc报文
分成两个部分，header和body
header可包含魔数、版本、消息类型、序列化类型、
报文体长度
body通常就是消息正文经过序列化存放的内容
### rpc序列化
rpc协议和序列化算法不是一回事，这点我以前经常混淆
如bolt是sofarpc设计的rpc远程调用时使用的协议
但是bolt中对消息的序列化算法使用的其实是hessian
常见的序列化方案
- 对象转json字符串再转byte数组
- java自带序列化：objectInputStream
- hessian算法

## rpc设计的组件划分
### 协议
#### message
用message来抽象rpc调用报文，报文通常要包括：
- interfaceName
- methodName
- 参数类型及参数值
- messageType 标识消息类型，不同消息其需包含的内容也不同
### 序列化器
如jdk自带的那套，可用这个对象进行抽象封装
### 编解码器
按照协议的设计，将接入的byte数组解码成message
或将message接出转化成byte数组
### messageHandler
正确的转化为message对象后，服务端要根据其中的要素，实现方法的执行
在java中最典型的方式就是fans，有了interfaceName、methodName和参数类型及参数值，怎么不行呢？

### 进一步的思考
- 服务名代表的组件，如xxxservice，应单例管理
- messageHandler后再抽离出具体的xxxMessageProcessor，根据具体的message类型进行分发调用
- rpc设计过程中大量组件都应单例管理，如serializer，可抽象出serializerManager统一管理这些单例