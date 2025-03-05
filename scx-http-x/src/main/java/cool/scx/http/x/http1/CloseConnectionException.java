package cool.scx.http.x.http1;

/// 主动关闭 Socket异常  (仅内部使用)
/// 调用方 当捕获到此异常的时候 应该直接断开连接
/// 被调用方 想中断 socket 连接 请抛出此异常
class CloseConnectionException extends RuntimeException {

    public CloseConnectionException() {

    }

    public CloseConnectionException(String message) {
        super(message);
    }

    public CloseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
