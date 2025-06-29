package cool.scx.tcp;

import cool.scx.functional.ScxConsumer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/// ScxTCPServer
///
/// 为什么没有 onError 回调 ?
///
/// ScxTCPServer 的主要职责是提供最小化, 底层的 socket 连接抽象, 只负责接收连接并交由用户处理.
///
/// 在 TCP 服务器的层面, 异常通常分为两类:
///
/// 1. **系统异常 (如 accept 异常) :**
///    这类错误一般无法被用户所干预, 只能记录日志并关闭 ServerSocket,
///    所以不需要额外回调来 "处理", 而且也没有什么可处理的.
///
/// 2. **用户处理器异常 (如用户在 onConnect 回调中抛出异常) :**
///    ScxTCPServer 设计允许用户在 onConnect 回调中直接抛异常,
///    这是为了防止用户遇到受检异常时不得不进行一次 "肮脏的 RuntimeException 包装" 而预留的方式.
///    当异常抛出时, 系统会记录日志, 并自动关闭对应 ServerSocket 以清理资源.
///
/// 若需要自定义异常处理逻辑 (如记录告警), 用户可自行在 onConnect 内部根据需要 try/catch 并执行自定义处理.
///
/// @author scx567888
/// @version 0.0.1
public interface ScxTCPServer {

    ScxTCPServer onConnect(ScxConsumer<ScxTCPSocket, ?> connectHandler);

    void start(SocketAddress localAddress) throws IOException;

    void stop();

    InetSocketAddress localAddress();

    default void start(int port) throws IOException {
        start(new InetSocketAddress(port));
    }

}
