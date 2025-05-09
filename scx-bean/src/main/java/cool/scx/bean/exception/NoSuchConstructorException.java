package cool.scx.bean.exception;

/// 没找到任何构造参数异常
public class NoSuchConstructorException extends RuntimeException {

    public NoSuchConstructorException(String message) {
        super(message);
    }
    
}
