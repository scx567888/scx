package cool.scx.exception;

/**
 * 用来将一些 检查型异常 包装为 运行时异常
 */
public class RuntimeExceptionWrapper extends RuntimeException {

    public RuntimeExceptionWrapper(Throwable cause) {
        super(cause);
    }

    public synchronized Throwable getDetail() {
        return super.getCause();
    }

}
