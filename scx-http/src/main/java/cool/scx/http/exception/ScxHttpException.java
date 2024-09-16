package cool.scx.http.exception;

import cool.scx.http.HttpStatusCode;

/**
 * <p>
 * 当我们的代码中有需要向客户端返回错误信息的时候
 * <p>
 * 推荐创建 HttpRequestException 的实现类并抛出异常 , 而不是手动进行异常的处理与响应的返回
 *
 * @author scx567888
 * @version 1.0.10
 */
public class ScxHttpException extends RuntimeException {

    /**
     * http 状态码
     */
    final HttpStatusCode statusCode;

    public ScxHttpException(HttpStatusCode statusCode) {
        super();
        this.statusCode = statusCode;
    }

    public ScxHttpException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public ScxHttpException(HttpStatusCode statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    public ScxHttpException(HttpStatusCode statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public final HttpStatusCode statusCode() {
        return this.statusCode;
    }

}
