package cool.scx.bytes.exception;

/// 数据拉取器异常
public class ByteSupplierException extends RuntimeException {

    public ByteSupplierException(String message) {
        super(message);
    }

    public ByteSupplierException(String message, Throwable cause) {
        super(message, cause);
    }

    public ByteSupplierException(Throwable cause) {
        super(cause);
    }

}
