package cool.scx.http.exception;

import cool.scx.http.status.ScxHttpStatus;

/// ScxHttpException
/// 这是一个基于 [ScxHttpStatus] 的运行时异常类, 用于表示 HTTP 请求处理过程中的异常情况.
///
/// 该异常旨在处理 HTTP 规范中 4xx（客户端错误）和 5xx（服务器错误）序列的状态码, 表示请求失败的各种可能原因.
/// 通过 [ScxHttpStatus] 提供了详细的状态码和描述信息, 使异常的语义更加清晰.
///
/// ### 注意:
///
///   - 200 系列状态码（表示请求成功）或其他非异常状态码通常不应使用此类来表示, 因为它们不属于异常的范畴.
///
/// ### 适用场景:
///
///   - 在 HTTP 请求处理失败时, 抛出此异常以便调用方捕获并作出相应处理.
///   - 结合 [ScxHttpStatus] 使用, 可以快速标识具体的错误类型并生成标准化的响应.
///
/// ### 使用示例:
/// ```java
/// throw new ScxHttpException(HttpStatus.NOT_FOUND, "Requested resource not found");
///```
///
/// @author scx567888
/// @version 0.0.1
public class ScxHttpException extends RuntimeException {

    private final ScxHttpStatus status;

    public ScxHttpException(ScxHttpStatus status) {
        super();
        this.status = status;
    }

    public ScxHttpException(ScxHttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public ScxHttpException(ScxHttpStatus status, Throwable cause) {
        super(cause);
        this.status = status;
    }

    public ScxHttpException(ScxHttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public final ScxHttpStatus status() {
        return this.status;
    }

}
