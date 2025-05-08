package cool.scx.bean.exception;

/// 未找到 对应的 Bean
public class NoSuchBeanException extends RuntimeException {

    public NoSuchBeanException(String message) {
        super(message);
    }

}
