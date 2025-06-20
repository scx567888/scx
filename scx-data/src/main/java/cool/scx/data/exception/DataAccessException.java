package cool.scx.data.exception;

/// 数据访问异常
///
/// @author scx567888
/// @version 0.0.1
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }

}
