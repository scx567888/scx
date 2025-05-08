package cool.scx.bean.exception;

/// 重复的 Bean 名称
public class DuplicateBeanNameException extends RuntimeException {
    
    public DuplicateBeanNameException(String message) {
        super(message);
    }
    
}
