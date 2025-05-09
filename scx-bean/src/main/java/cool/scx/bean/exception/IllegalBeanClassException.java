package cool.scx.bean.exception;

/// 非法的 bean 类型 比如说 接口
public class IllegalBeanClassException extends RuntimeException {
    
    public IllegalBeanClassException(String message) {
        super(message);
    }
    
}
