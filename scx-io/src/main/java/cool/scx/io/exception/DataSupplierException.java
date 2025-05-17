package cool.scx.io.exception;

/// 数据拉取器异常
public class DataSupplierException extends RuntimeException {

    public DataSupplierException(String message) {
        super(message);
    }

    public DataSupplierException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSupplierException(Throwable cause) {
        super(cause);
    }

}
