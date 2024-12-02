package cool.scx.common.exception;

/**
 * ScxRuntimeException 包装一个异常 到 运行时异常
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxRuntimeException extends RuntimeException {

    public ScxRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScxRuntimeException(Throwable cause) {
        super(cause);
    }

}
