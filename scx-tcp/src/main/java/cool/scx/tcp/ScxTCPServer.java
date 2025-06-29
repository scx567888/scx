package cool.scx.tcp;

import cool.scx.functional.ScxConsumer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/// ScxTCPServer
///
///
/// 为什么没有 onError 回调 ?
///
/// ScxTCPServer 的主要职责是提供最小化、底层的 socket 连接抽象，只负责接收连接并交由用户处理。
///
/// 在 TCP 服务器层面，异常通常分为两类：
///
/// 1. **系统异常（如 accept 异常）**
///    这类错误一般无法被用户所干预，只能记录日志并关闭 ServerSocket，
///    不需要额外回调来 "处理"，也没有可供处理的上下文。
///
/// 2. **用户处理器异常（如用户在 onConnect 回调中抛出异常）**
///    在 TCP 这种事件回调场景中，用户处理器异常通常并没有有意义的上下文，
///    用户除了当前的 Socket 之外，无法感知更多信息，且该连接往往已经关闭，
///    即使提供异常回调，能做的事情也极为有限。
///
///    至于为什么允许用户在 onConnect 回调中抛出异常，
///    在实际使用中，Socket 操作总会不可避免地遇到 IO 异常，
///    这通常意味着当前连接已经无效，也不再需要继续处理。
///
///    因此，允许用户在 onConnect 中直接抛出异常，
///    既能保留最原始的异常信息，方便排查，
///    又避免了被迫将受检异常包装成 RuntimeException，
///    从而提高可读性和调试体验。
///
///    大多数情况下，仅在内部记录日志即可满足需求，
///    引入 onError 回调不仅增加理解成本，
///    反而容易误导用户去做一些实际无效的异常处理逻辑。
///
/// 若需要自定义异常处理（例如告警或统计），用户可以在 onConnect 内自行 try/catch 并执行相应逻辑。
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
