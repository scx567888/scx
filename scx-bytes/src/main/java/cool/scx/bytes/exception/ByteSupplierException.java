package cool.scx.bytes.exception;

/// ByteSupplierException
///
/// @author scx567888
/// @version 0.0.1
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
