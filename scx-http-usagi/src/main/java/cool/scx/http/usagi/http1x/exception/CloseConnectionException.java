package cool.scx.http.usagi.http1x.exception;

/**
 * Socket 关闭异常 当捕获到此异常的时候 应该直接断开连接 (仅内部使用)
 */
public class CloseConnectionException extends RuntimeException {

}
