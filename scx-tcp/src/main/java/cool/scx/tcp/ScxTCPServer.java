package cool.scx.tcp;

import cool.scx.functional.ScxConsumer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/// ScxTCPServer
///
/// 为什么没有 onError 回调 ?
///
/// ScxTCPServer 的本质是提供最小化, 底层的 Socket 连接抽象, 只负责接收连接并交由用户处理.
///
/// 在 TCP 服务器层面, 异常通常分为两类:
///
/// 1. **系统异常 (如 accept 异常)**
///     此类异常通常是由于系统资源耗尽, 监听端口被强制关闭等不可恢复性错误导致,
///     对用户而言, 由于没有可操作的上下文, 一般无法被干预, 仅需内部记录日志并关闭 ServerSocket 即可.
///     因此无需暴露额外的 onError 回调来 "处理" 这些异常, 而且即使暴漏也没什么可处理的.
///
/// 2. **用户处理器异常 (如用户在 onConnect 回调中抛出异常) **
///    在 TCP 这种事件回调场景中, 用户处理器异常通常并没有有意义的上下文,
///    用户除了当前的 Socket 之外, 无法感知更多信息, 且该连接往往已经关闭，
///    即使提 onError 回调, 能做的事情实际上也极为有限.
///
///    至于为什么没有 onError 却又允许用户在 onConnect 回调中抛出异常,
///    这是因为在实际使用中吗, Socket 操作总会不可避免地遇到 IO 异常,
///    这通常意味着当前连接已经无效, 也不再需要继续处理.
///
///    因此, 允许用户在 onConnect 中直接抛出异常, 主要是为了能保留最原始的异常信息,
///    且避免了用户被迫将 受检异常 包装成 RuntimeException, 从而减少异常信息的无用包装层级.
///
///    实际上, 绝大多数情况下, 仅在内部记录日志即可满足需求, 引入 onError 回调不仅增加理解成本,
///    反而容易误导用户去做一些实际无效的异常处理逻辑.
///
/// 若需要自定义异常处理 (例如告警, 统计等), 可在 onConnect 内自行 try/catch 并实现相应逻辑。
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
