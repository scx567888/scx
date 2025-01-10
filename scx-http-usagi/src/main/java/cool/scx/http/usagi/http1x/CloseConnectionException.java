package cool.scx.http.usagi.http1x;

/**
 * Socket 关闭异常 当捕获到此异常的时候 应该直接断开连接 (仅内部使用)
 */
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
