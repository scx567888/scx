package cool.scx.object.mapping;

/// 映射异常
public class NodeMappingException extends RuntimeException {

    public NodeMappingException(String message) {
        super(message);
    }

    public NodeMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public NodeMappingException(Throwable cause) {
        super(cause);
    }

}
