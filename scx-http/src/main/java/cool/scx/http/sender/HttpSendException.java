package cool.scx.http.sender;

/// 发送异常 一般表示底层异常
public class HttpSendException extends RuntimeException {

    public HttpSendException(Throwable cause) {
        super(cause);
    }

    public HttpSendException(String message, Throwable cause) {
        super(message, cause);
    }

}
